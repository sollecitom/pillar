dependencies {
    api(libs.swissknife.pagination.domain)
    api(libs.swissknife.json.utils)
    api(projects.jsonSerializationCore)

    implementation(libs.acme.schema.catalogue.json.common)
    implementation(libs.swissknife.kotlin.extensions)

    testImplementation(projects.jsonSerializationTestUtils)
    testImplementation(libs.swissknife.pagination.test.utils)
    testImplementation(libs.swissknife.core.test.utils)
    testImplementation(libs.swissknife.test.utils)
}