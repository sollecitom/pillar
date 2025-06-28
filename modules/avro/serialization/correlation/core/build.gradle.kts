dependencies {
    api(libs.swissknife.correlation.core.domain)
    api(libs.swissknife.avro.serialization.utils)
    api(libs.swissknife.avro.schema.catalogue.domain)

    implementation(projects.avroSerializationCore)
    implementation(libs.swissknife.kotlin.extensions)

    testImplementation(projects.avroSerializationTestUtils)
    testImplementation(libs.swissknife.correlation.core.test.utils)
    testImplementation(libs.swissknife.avro.schema.catalogue.test.utils)
    testImplementation(libs.swissknife.test.utils)
}