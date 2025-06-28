dependencies {
    api(projects.correlationLoggingUtils)
    api(libs.swissknife.correlation.core.domain)
    api(libs.swissknife.json.test.utils)
    api(libs.swissknife.correlation.core.test.utils)
    api(libs.swissknife.core.test.utils)
    api(libs.swissknife.test.utils)

    implementation(projects.jsonSerializationCorrelationCore)
    implementation(libs.swissknife.kotlin.extensions)
}