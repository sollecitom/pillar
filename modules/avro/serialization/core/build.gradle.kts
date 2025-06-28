dependencies {
    api(libs.swissknife.core.domain)
    api(libs.swissknife.avro.serialization.utils)
    api(libs.swissknife.avro.schema.catalogue.domain)

    api(libs.acme.schema.catalogue.avro.common)

    implementation(libs.swissknife.kotlin.extensions)

    testImplementation(projects.avroSerializationTestUtils)
    testImplementation(libs.swissknife.avro.schema.catalogue.test.utils)
    testImplementation(libs.swissknife.core.test.utils)
    testImplementation(libs.swissknife.test.utils)
}