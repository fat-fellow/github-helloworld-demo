import com.android.build.api.dsl.ApplicationExtension

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