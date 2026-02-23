plugins {
    alias(libs.plugins.mayudin.android.feature)
}

dependencies {
    implementation(project(":common:network"))
    implementation(libs.androidx.material.icons.extended)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
