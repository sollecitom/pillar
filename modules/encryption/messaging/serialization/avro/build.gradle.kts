dependencies {
    api(libs.swissknife.avro.serialization.utils)
    api(libs.swissknife.avro.schema.catalogue.domain)
    api(libs.swissknife.cryptography.domain)

    implementation(libs.swissknife.kotlin.extensions)
    implementation(libs.acme.schema.catalogue.avro.common)

    testImplementation(libs.swissknife.test.utils)
    testImplementation(projects.avroSerializationTestUtils)
    testImplementation(libs.swissknife.avro.schema.catalogue.test.utils)
    testImplementation(libs.swissknife.cryptography.implementation.bouncy.castle)
}