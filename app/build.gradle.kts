import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.time.LocalDate

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.androidGitVersion)
    alias(libs.plugins.hydraulicConveyor)
}

androidGitVersion {
    format = "%tag%"
}

version = androidGitVersion.name()

logger.lifecycle("App version $version (Code: ${androidGitVersion.code()})")

kotlin {

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm()

    jvmToolchain(jdkVersion = 17)

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {

        moduleName = "app"

        browser {

            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path

            commonWebpackConfig {
                outputFileName = "app.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }

        binaries.executable()
    }

    sourceSets {

        commonMain.dependencies {

            /* Compose UI */
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)

            /* Platform Tools */
            implementation(libs.platformtools.core)
            implementation(libs.platformtools.darkmodedetector)

            /* Coroutines */
            implementation(libs.kotlinx.coroutines.core)

            /* Datetime */
            implementation(libs.kotlinx.datetime)

            /* Settings */
            implementation(libs.multiplatformSettings)

            /* Lottie Animations */
            implementation(libs.compottie)
            implementation(libs.compottie.dot)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }

        jvmTest.dependencies {
            implementation(libs.kotlin.test.junit)
            implementation(compose.desktop.currentOs)
            implementation(compose.desktop.uiTestJUnit4)
        }
    }
}

android {

    namespace = "de.stefan_oltmann.mines"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {

        applicationId = "de.stefan_oltmann.mines"

        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()

        if (androidGitVersion.code() == 0) {

            /* Values for the dev version. */
            versionName = "1.0.0"
            versionCode = 1

        } else {

            versionName = androidGitVersion.name()
            versionCode = androidGitVersion.code()
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            /*
             * As an open source project we don't need ProGuard.
             */
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

compose.desktop {

    application {

        mainClass = "de.stefan_oltmann.mines.MainKt"

        nativeDistributions {

            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

            packageName = "Mines"

            if (androidGitVersion.code() == 0) {

                /* Values for the dev version. */
                packageVersion = "1.0.0"

            } else {

                packageVersion = version.toString()
            }

            macOS {
                iconFile.set(project.file("../icon/icon.icns"))
            }

            windows {
                iconFile.set(project.file("../icon/icon.ico"))
            }

            linux {
                iconFile.set(project.file("../icon/icon.png"))
            }

            buildTypes.release.proguard {
                isEnabled = true
                obfuscate.set(false)
                optimize.set(true)
                configurationFiles.from(project.file("proguard-rules.pro"))
            }
        }
    }
}

dependencies {

    implementation(libs.androidx.runtime.android)
    debugImplementation(compose.uiTooling)

    linuxAmd64(compose.desktop.linux_x64)
    macAmd64(compose.desktop.macos_x64)
    macAarch64(compose.desktop.macos_arm64)
    windowsAmd64(compose.desktop.windows_x64)
}

// region Work around temporary Compose bugs.
configurations.all {
    attributes {
        // https://github.com/JetBrains/compose-jb/issues/1404#issuecomment-1146894731
        attribute(Attribute.of("ui", String::class.java), "awt")
    }
}
// endregion

/*
 * Tasks for flatpak release
 */
tasks {

    val appId = "de.stefan_oltmann.mines"

    /*
     * Get the tag from GitHub Actions environment variable if available, otherwise use version.toString()
     */
    val appVersion = System.getenv("GITHUB_REF")?.let { ref ->
        if (ref.startsWith("refs/tags/")) {
            val tag = ref.removePrefix("refs/tags/")
            logger.lifecycle("Using tag from GitHub Actions: $tag")
            tag
        } else {
            logger.lifecycle("GITHUB_REF doesn't start with 'refs/tags/': $ref, falling back to version.toString()")
            version.toString()
        }
    } ?: run {
        logger.lifecycle("GITHUB_REF not found, falling back to version.toString()")
        version.toString()
    }

    /*
     * Common constants for flatpak tasks
     */
    val metainfoPath = "${rootDir}/flatpak/$appId.metainfo.xml"

    /*
     * Helper function to check if running on Linux
     */
    fun isLinux(): Boolean {
        val osName = System.getProperty("os.name").lowercase()
        return osName.contains("linux")
    }

    /*
     * Helper function to check if flatpak-builder is installed
     */
    fun isFlatpakBuilderInstalled(): Boolean {
        return try {
            val process = ProcessBuilder("which", "flatpak-builder")
                .redirectErrorStream(true)
                .start()
            process.waitFor()
            process.exitValue() == 0
        } catch (e: Exception) {
            logger.warn("Error checking for flatpak-builder: ${e.message}")
            false
        }
    }

    /*
     * Helper functions for XML operations
     */
    fun parseXmlFile(xmlFilePath: String): Pair<javax.xml.parsers.DocumentBuilder, org.w3c.dom.Document> {
        val xmlFile = file(xmlFilePath)
        require(xmlFile.exists()) { "XML file not found: $xmlFilePath" }

        val docBuilder = javax.xml.parsers.DocumentBuilderFactory
            .newInstance()
            .apply { isNamespaceAware = true }
            .newDocumentBuilder()
        val doc = docBuilder.parse(xmlFile)
        doc.documentElement.normalize()

        return docBuilder to doc
    }

    fun getOrCreateReleasesElement(doc: org.w3c.dom.Document): org.w3c.dom.Element {
        return doc.getElementsByTagName("releases")
            .let { if (it.length > 0) it.item(0) else null }
            ?.let { it as org.w3c.dom.Element }
            ?: doc.createElement("releases").also {
                doc.documentElement.appendChild(it)
            }
    }

    fun saveXmlDocument(doc: org.w3c.dom.Document, xmlFilePath: String) {
        val xmlFile = file(xmlFilePath)
        javax.xml.transform.TransformerFactory
            .newInstance()
            .newTransformer()
            .apply {
                // Explicitly disable indentation and whitespace formatting
                setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "no")
                setOutputProperty(javax.xml.transform.OutputKeys.METHOD, "xml")
                setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "no")
            }
            .transform(
                javax.xml.transform.dom.DOMSource(doc),
                javax.xml.transform.stream.StreamResult(xmlFile)
            )
    }

    fun logInfo(taskName: String, message: String) {
        println("$taskName -- INFO -- $message")
    }

    val packageTarReleaseDistributable by registering(Tar::class) {
        group = "compose desktop"
        description = "Creates the tar.gz archive of the release binary"
        from(named("createReleaseDistributable"))
        archiveBaseName = "de.stefan_oltmann.mines"
        archiveVersion = appVersion
        compression = Compression.GZIP
        archiveExtension = "tar.gz"
        // The resulting filename will be de.stefan_oltmann.mines-{version}.tar.gz
    }

    val createFlatpakTarLink by registering {
        group = "compose desktop"
        description = "Creates a copy of the tar.gz file with the name app-linux.tar.gz for Flatpak builds"
        dependsOn(packageTarReleaseDistributable)

        doLast {
            val sourceFile = packageTarReleaseDistributable.get().archiveFile.get().asFile
            val targetFile = File(sourceFile.parentFile, "app-linux.tar.gz")

            if (targetFile.exists()) {
                targetFile.delete()
            }

            sourceFile.copyTo(targetFile, overwrite = true)
            logger.lifecycle("Created Flatpak-compatible tar.gz file: ${targetFile.absolutePath}")
        }
    }

    /**
     * Updates the metainfo.xml file with the current version and date
     */
    val updateMetainfo by registering {
        group = "compose desktop"
        description = "Updates metainfo.xml with the current version and date"

        inputs.file(metainfoPath)
        outputs.file(metainfoPath)

        doLast {
            val (_, doc) = parseXmlFile(metainfoPath)
            val releasesElem = getOrCreateReleasesElement(doc)

            // --- Check if the version already exists ---
            val versionExists = (0 until releasesElem.childNodes.length)
                .asSequence()
                .map { releasesElem.childNodes.item(it) }
                .filter { it.nodeType == org.w3c.dom.Node.ELEMENT_NODE }
                .map { it as org.w3c.dom.Element }
                .any { it.getAttribute("version") == appVersion }

            if (versionExists) {
                logInfo("updateMetainfo", "Release $appVersion is already listed, no changes made.")
            } else {
                // --- Add new <release> block ---
                val today = LocalDate.now().toString() // YYYY-MM-DD
                val release = doc.createElement("release").apply {
                    setAttribute("version", appVersion)
                    setAttribute("date", today)
                }
                // Insert at the top to keep descending order
                releasesElem.insertBefore(release, releasesElem.firstChild)
                logInfo("updateMetainfo", "Added release entry $appVersion ($today).")
            }

            saveXmlDocument(doc, metainfoPath)
        }
    }

    /**
     * Single-step task: build + finish + export to repo
     */
    val buildFlatpak by registering {
        group = "compose desktop"
        description = "Builds and exports the Flatpak into the local repo"
        dependsOn(createFlatpakTarLink, updateMetainfo)

        doLast {
            if (!isLinux()) {
                logger.lifecycle("Skipping Flatpak build: Not running on Linux")
                return@doLast
            }

            if (!isFlatpakBuilderInstalled()) {
                logger.error("Flatpak build failed: flatpak-builder is not installed")
                throw GradleException("flatpak-builder is not installed. Please install it to build Flatpak packages.")
            }

            logInfo("buildFlatpak", "Building + exporting")

            val process = ProcessBuilder(
                "flatpak-builder",
                "--user",
                "--force-clean",
                "--install-deps-from=flathub",
                "--disable-rofiles-fuse",
                "--repo=${rootDir}/repo", // absolute path recommended
                "build-dir",
                "${rootDir}/flatpak/$appId.yml"
            )
                .redirectErrorStream(true)
                .start()

            // Log the output
            val reader = process.inputStream.bufferedReader()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                logger.lifecycle(line)
            }

            val exitCode = process.waitFor()
            if (exitCode != 0) {
                throw GradleException("Flatpak build failed with exit code $exitCode")
            }
        }
    }

    /**
     * Creates the .flatpak file from the repo
     */
    val bundleFlatpak by registering {
        group = "compose desktop"
        description = "Generates the final .flatpak file"
        dependsOn(buildFlatpak)

        doLast {
            if (!isLinux()) {
                logger.lifecycle("Skipping Flatpak bundle: Not running on Linux")
                return@doLast
            }

            val outputDir = file("${layout.buildDirectory.get()}/flatpak").apply { mkdirs() }
            logInfo("bundleFlatpak", "Output: $outputDir")

            val process = ProcessBuilder(
                "flatpak", "build-bundle",
                "repo",
                "$outputDir/$appId-$appVersion.flatpak",
                appId
            )
                .directory(rootDir)
                .redirectErrorStream(true)
                .start()

            // Log the output
            val reader = process.inputStream.bufferedReader()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                logger.lifecycle(line)
            }

            val exitCode = process.waitFor()
            if (exitCode != 0) {
                throw GradleException("Flatpak bundle failed with exit code $exitCode")
            }
        }
    }

    /**
     * Task to clean up the releases in metainfo.xml after Flatpak build
     */
    val cleanupFlatpakReleases by registering {
        group = "compose desktop"
        description = "Cleans up releases in metainfo.xml after Flatpak build"
        dependsOn(bundleFlatpak)

        inputs.file(metainfoPath)
        outputs.file(metainfoPath)

        doLast {
            if (!isLinux()) {
                logger.lifecycle("Skipping Flatpak releases cleanup: Not running on Linux")
                return@doLast
            }

            val (_, doc) = parseXmlFile(metainfoPath)
            val releasesElem = getOrCreateReleasesElement(doc)

            // --- Remove all child nodes from releases element ---
            while (releasesElem.hasChildNodes()) {
                releasesElem.removeChild(releasesElem.firstChild)
            }

            logInfo("cleanupFlatpakReleases", "Cleaned up releases in metainfo.xml")

            saveXmlDocument(doc, metainfoPath)
        }
    }

    /**
     * Cleanup task to remove temporary directories after Flatpak build
     */
    val cleanupFlatpakBuild by registering {
        group = "compose desktop"
        description = "Cleans up temporary directories after Flatpak build"
        dependsOn(cleanupFlatpakReleases)

        doLast {
            if (!isLinux()) {
                logger.lifecycle("Skipping Flatpak build cleanup: Not running on Linux")
                return@doLast
            }

            logInfo("cleanupFlatpakBuild", "Cleaning up temporary directories")

            val dirsToDelete = listOf(
                "${project.projectDir}/.flatpak-builder",
                "${project.projectDir}/build-dir",
                "${project.projectDir}/repo",
                "${rootDir}/repo"
            )

            dirsToDelete.forEach { dirPath ->
                val dir = file(dirPath)
                if (dir.exists()) {
                    if (dir.deleteRecursively()) {
                        logger.lifecycle("Deleted: $dirPath")
                    } else {
                        logger.warn("Failed to delete: $dirPath")
                    }
                }
            }
        }
    }

    /**
     * Public entry task
     */
    register("packageFlatpakReleaseDistributable") {
        group = "compose desktop"
        description = "Full chain: tar.gz + flatpak + cleanup"
        dependsOn(cleanupFlatpakBuild)
    }
}
