plugins {
    alias(libs.plugins.mayudin.android.feature)
}

dependencies {
    implementation(project(":common:network"))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
}