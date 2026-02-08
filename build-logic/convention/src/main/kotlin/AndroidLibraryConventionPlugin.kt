/*
 * Copyright 2022 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import mayudin.convention.configureKotlinAndroid
import mayudin.convention.disableUnnecessaryAndroidTests
import mayudin.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.android")
            apply(plugin = "mayudin.convention.android.lint")
            apply(plugin = "mayudin.convention.android.jacoco")

            extensions.configure<LibraryExtension> {
                namespace = path.split("""\W""".toRegex()).drop(1).distinct().joinToString(separator = ".").lowercase()
                configureKotlinAndroid(this)
                testOptions.targetSdk = project.libs.findVersion("targetSdk").get().requiredVersion.toInt()
                lint.targetSdk = project.libs.findVersion("targetSdk").get().requiredVersion.toInt()
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                testOptions.animationsDisabled = true
                buildFeatures.buildConfig = false
                // The resource prefix is derived from the module name,
                // so resources inside ":core:module1" must be prefixed with "core_module1_"
                resourcePrefix =
                    path.split("""\W""".toRegex()).drop(1).distinct().joinToString(separator = "_")
                        .lowercase() + "_"

                packaging {
                    resources {
                        excludes.addAll(
                            listOf(
                                "**/attach_hotspot_windows.dll",
                                "META-INF/licenses/ASM",
                                "META-INF/LICENSE.md",
                                "META-INF/LICENSE-notice.md",
                                "META-INF/LICENSE",
                                "META-INF/LICENSE.txt",
                                "META-INF/license.txt",
                                "META-INF/NOTICE",
                                "META-INF/NOTICE.txt",
                                "META-INF/DEPENDENCIES",
                                "META-INF/notice.txt",
                                "META-INF/ASL2.0",
                                "META-INF/LGPL2.1",
                                "META-INF/AL2.0",
                            ),
                        )
                    }
                }
            }

            dependencies {
                "implementation"(libs.findLibrary("kotlinx.coroutines.core").get())
                "testImplementation"(libs.findLibrary("kotlin.test").get())
                "testImplementation"(libs.findLibrary("junit").get())
                "testImplementation"(libs.findLibrary("mockk").get())
                "testImplementation"(libs.findLibrary("kotlinx.coroutines.test").get())
                "testImplementation"(libs.findLibrary("test.turbine").get())
            }
        }
    }
}
