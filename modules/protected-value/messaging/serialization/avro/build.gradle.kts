dependencies {
    api(projects.protectedValueDomain)
    api(libs.swissknife.avro.serialization.utils)
    api(libs.swissknife.avro.schema.catalogue.domain)

    implementation(libs.swissknife.kotlin.extensions)
    implementation(projects.encryptionMessagingSerializationAvro)
    implementation(libs.acme.schema.catalogue.avro.common)
    implementation(projects.avroSerializationCore)

    testImplementation(libs.swissknife.test.utils)
    testImplementation(projects.avroSerializationTestUtils)
    testImplementation(libs.swissknife.avro.schema.catalogue.test.utils)
    testImplementation(libs.swissknife.cryptography.implementation.bouncy.castle)
    testImplementation(libs.swissknife.protected.value.factory.aes)
}