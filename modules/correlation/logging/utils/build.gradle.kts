plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(libs.swissknife.correlation.logging.utils)

    implementation(projects.jsonSerializationCorrelationCore)
}