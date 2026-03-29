plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(libs.swissknife.cryptography.domain)
    api(libs.swissknife.protected.value.domain)

    implementation(libs.swissknife.kotlin.extensions)

    testImplementation(libs.swissknife.test.utils)
}