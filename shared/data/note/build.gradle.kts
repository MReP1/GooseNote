plugins {
    alias(libs.plugins.goose.kotlin.multiplatform)
    alias(libs.plugins.goose.room.multiplatform)
}

kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DataNote"
            isStatic = true
            linkerOpts.add("-lsqlite3")
        }
    }

    sourceSets {
        commonMain.dependencies {
            compileOnly(libs.koin.core)
            implementation(project(":shared:common"))
            api(project(":shared:data:database"))
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
        iosMain.dependencies {
        }
    }

}

android {
    namespace = "little.goose.data.note"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
}

room {
    schemaDirectory("$projectDir/schemas")
}