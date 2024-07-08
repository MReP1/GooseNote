package plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@Suppress("unused")
class RoomMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("ksp").get().get().pluginId)
                apply(libs.findPlugin("room").get().get().pluginId)
            }

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    commonMain {
                        dependencies {
                            implementation(libs.findLibrary("room.runtime").get())
                            implementation(libs.findLibrary("sqlite.bundled").get())
                        }
                    }
                }
            }

            dependencies {
                add("ksp", libs.findLibrary("room-compiler").get())
            }
        }
    }
}