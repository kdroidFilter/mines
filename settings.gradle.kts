rootProject.name = "mines"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("./offline-repository")
        maven { url = uri("/app/offline-repository") }   // en prod, chemin absolu
        maven { url = uri("file:offline-repository") }
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        maven("./offline-repository")
        maven { url = uri("/app/offline-repository") }   // en prod, chemin absolu
        maven { url = uri("file:offline-repository") }
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":app")
