dependencies {
    api(projects.jsonSerializationCore)
    api(libs.swissknife.web.api.domain)

    implementation(libs.acme.schema.catalogue.json.common)
    implementation(libs.swissknife.kotlin.extensions)

    testImplementation(projects.jsonSerializationTestUtils)
    testImplementation(libs.swissknife.correlation.core.test.utils)
    testImplementation(libs.swissknife.core.test.utils)
    testImplementation(libs.swissknife.test.utils)
}
