dependencies {
    api(projects.pillarProtectedValueDomain)
    api(projects.swissknifeAvroSerializationUtils)
    api(projects.swissknifeAvroSchemaCatalogueDomain)

    implementation(projects.swissknifeKotlinExtensions)
    implementation(projects.pillarEncryptionMessagingSerializationAvro)
    implementation(projects.acmeSchemataAvroCommon)
    implementation(projects.pillarAvroSerializationCore)

    testImplementation(projects.swissknifeTestUtils)
    testImplementation(projects.pillarAvroSerializationTestUtils)
    testImplementation(projects.swissknifeAvroSchemaCatalogueTestUtils)
    testImplementation(projects.swissknifeCryptographyImplementationBouncyCastle)
    testImplementation(projects.swissknifeProtectedValueFactoryAes)
}