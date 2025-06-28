dependencies {
    api(libs.swissknife.correlation.core.domain)
    api(libs.swissknife.json.utils)
    api(projects.jsonSerializationCore)

    implementation(libs.swissknife.kotlin.extensions)
    implementation(libs.acme.schema.catalogue.json.common)

    testImplementation(projects.jsonSerializationTestUtils)
    testImplementation(libs.swissknife.correlation.core.test.utils)
    testImplementation(libs.swissknife.core.test.utils)
    testImplementation(libs.swissknife.test.utils)
}