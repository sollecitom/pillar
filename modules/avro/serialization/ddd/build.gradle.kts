dependencies {
    api(libs.swissknife.ddd.domain)
    api(libs.swissknife.avro.serialization.utils)
    api(libs.swissknife.avro.schema.catalogue.domain)

    implementation(projects.avroSerializationCorrelationCore)
    implementation(projects.avroSerializationCore)
    implementation(libs.swissknife.kotlin.extensions)

    testImplementation(projects.avroSerializationTestUtils)
    testImplementation(libs.swissknife.ddd.test.utils)
    testImplementation(libs.swissknife.avro.schema.catalogue.test.utils)
    testImplementation(libs.swissknife.test.utils)
}