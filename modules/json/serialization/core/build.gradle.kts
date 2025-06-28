dependencies {
    api(libs.swissknife.core.domain)
    api(libs.swissknife.json.utils)

    implementation(libs.swissknife.kotlin.extensions)
    implementation(libs.acme.schema.catalogue.json.common)

    testImplementation(projects.jsonSerializationTestUtils)
    testImplementation(libs.swissknife.core.test.utils)
    testImplementation(libs.swissknife.test.utils)
}