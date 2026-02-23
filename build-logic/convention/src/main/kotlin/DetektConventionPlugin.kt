import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("dev.detekt")

            val configFile = target.rootProject
                .layout
                .projectDirectory
                .file("config/detekt/detekt.yml")

            configure<DetektExtension> {
                buildUponDefaultConfig.set(true)
                allRules.set(false)
                config.setFrom(configFile)
            }

            tasks.withType<Detekt>().configureEach {
                autoCorrect.set(true)
                reports {
                    html.required.set(true)
                }
            }
        }
    }
}

