package plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
class ComposeConventionPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply(
                    libs.findPlugin("jetbrains-compose-compiler")
                        .get().get().pluginId
                )
            }
            val bom = libs.findLibrary("androidx-compose-bom").get()
            dependencies {
                add("implementation", platform(bom))
                add("androidTestImplementation", platform(bom))
            }
            applyComposeStrongSkippingMode()
        }
    }

}