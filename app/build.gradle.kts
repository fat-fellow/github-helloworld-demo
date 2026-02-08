import com.android.build.api.dsl.ApplicationExtension
import dev.detekt.gradle.Detekt

plugins {
    alias(libs.plugins.mayudin.android.application)
    alias(libs.plugins.detekt)
}

configure<ApplicationExtension> {
    namespace = "mayudin.helloworld"

    defaultConfig {
        applicationId = "mayudin.helloworld"
        versionCode = 1
        versionName = "1.0"
    }
}

allprojects {
    // worth moving to convention plugin
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    detekt {
        buildUponDefaultConfig = true
        allRules = false
        config.setFrom("$rootDir/config/detekt/detekt.yml")
    }

    tasks.withType<Detekt>().configureEach {
        reports {
            html.required.set(true)
        }
    }
}

dependencies {
    implementation(project(":common:network"))
    implementation(project(":feature:info"))
    implementation(project(":feature:repos"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}