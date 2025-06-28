dependencies {
    api(libs.swissknife.ddd.domain)

    implementation(libs.acme.schema.catalogue.json.common)
    implementation(projects.jsonSerializationCorrelationCore)

    testImplementation(projects.jsonSerializationTestUtils)
    testImplementation(libs.swissknife.core.test.utils)
    testImplementation(libs.swissknife.correlation.core.test.utils)
    testImplementation(libs.swissknife.test.utils)
}