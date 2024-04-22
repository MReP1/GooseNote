includeBuild("build-logic")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "note"
include(":shared:common")
include(":note")
include(":shared:data:note")
include(":shared:data:database")
include(":shared:feature:note")
include(":shared:design")
include(":shared:ui")
include(":shared:resource")