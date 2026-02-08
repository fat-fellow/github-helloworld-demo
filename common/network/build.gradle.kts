plugins {
    alias(libs.plugins.mayudin.android.library)
    alias(libs.plugins.mayudin.android.hilt)
}

dependencies {
    api(project(":common:domain"))
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp3)
    api(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.content.negotiation)
}