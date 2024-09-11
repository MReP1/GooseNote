plugins {
    alias(libs.plugins.goose.kotlin.multiplatform)
    alias(libs.plugins.goose.compose.multiplatform)
}

kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "FeatureNote"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.compose.markdown)
            implementation(libs.compose.markdown.m3)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(project(":shared:common"))
            implementation(project(":shared:data:note"))
            implementation(project(":shared:design"))
            implementation(project(":shared:ui"))
            implementation(project(":shared:resource"))
            implementation(libs.koin.compose)
            implementation(libs.compose.navigation)
            implementation(libs.compose.viewModel)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
        iosMain.dependencies {
        }
    }

}


android {
    namespace = "little.goose.feature.note"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")

    dependencies {
        debugImplementation(libs.androidx.compose.ui.tooling)
    }
}