plugins {
    `kotlin-dsl`
}

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.kotlin.compose.compiler.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = libs.plugins.goose.android.library.get().pluginId
            implementationClass = "plugin.AndroidLibraryConventionPlugin"
        }
        register("androidApp") {
            id = libs.plugins.goose.android.application.get().pluginId
            implementationClass = "plugin.AndroidAppConventionPlugin"
        }
        register("kotlinMultiplatform") {
            id = libs.plugins.goose.kotlin.multiplatform.get().pluginId
            implementationClass = "plugin.KotlinMultiplatformConventionPlugin"
        }
        register("composeMultiplatform") {
            id = libs.plugins.goose.compose.multiplatform.get().pluginId
            implementationClass = "plugin.KmpComposePlugin"
        }
        register("roomMultiplatform") {
            id = libs.plugins.goose.room.multiplatform.get().pluginId
            implementationClass = "plugin.RoomMultiplatformPlugin"
        }
    }
}