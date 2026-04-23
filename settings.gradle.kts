pluginManagement {
    repositories {
        mavenLocal()
//        maven {
//            url = uri("https://androidx.dev/snapshots/builds/13508953/artifacts/repository")
//        }
        maven("https://maven.myket.ir")
        maven("https://gradle.jamko.ir")
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven("https://jitpack.io")
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
//        maven {
//            url = uri("https://androidx.dev/snapshots/builds/13508953/artifacts/repository")
//        }
        maven("https://maven.myket.ir")
        maven("https://gradle.jamko.ir")
        google()
        maven("https://jitpack.io")
        mavenCentral()
    }
}

rootProject.name = "CleanComposeStore"
include(":app")
 