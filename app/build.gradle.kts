import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

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

tasks {

    val appId = "de.stefan_oltmann.mines"
    val appVersion = version.toString()

    val packageTarReleaseDistributable by registering(Tar::class) {
        group = "compose desktop"
        description = "Creates the tar.gz archive of the release binary"
        from(named("createReleaseDistributable"))
        archiveBaseName = "Mines"
        archiveClassifier = "linux"
        compression = Compression.GZIP
        archiveExtension = "tar.gz"
        archiveFileName = "Mines-linux.tar.gz"
    }

    /**
     * Single-step task: build + finish + export to repo
     */
    val buildFlatpak by registering(Exec::class) {
        group = "compose desktop"
        description = "Builds and exports the Flatpak into the local repo"
        dependsOn(packageTarReleaseDistributable)
        commandLine(
            "flatpak-builder",
            "--user",
            "--force-clean",
            "--install-deps-from=flathub",
            "--disable-rofiles-fuse",
            "--repo=${rootDir}/repo", // absolute path recommended
            "build-dir",
            "${project.projectDir}/src/jvmMain/$appId.yml"
        )
        doFirst { println("buildFlatpak -- INFO -- Building + exporting") }
    }

    /**
     * Creates the .flatpak file from the repo
     */
    val bundleFlatpak by registering(Exec::class) {
        group = "compose desktop"
        description = "Generates the final .flatpak file"
        dependsOn(buildFlatpak)
        workingDir(rootDir)
        doFirst {
            val outputDir = file("${layout.buildDirectory.get()}/flatpak").apply { mkdirs() }
            println("bundleFlatpak -- INFO -- Output: $outputDir")
            commandLine(
                "flatpak", "build-bundle",
                "repo",
                "$outputDir/$appId-$appVersion.flatpak",
                appId
            )
        }
    }

    /**
     * Public entry task
     */
    register("packageFlatpakReleaseDistributable") {
        group = "compose desktop"
        description = "Full chain: tar.gz + flatpak"
        dependsOn(bundleFlatpak)
    }
}
