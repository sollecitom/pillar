dependencies {
    api(projects.swissknifeAvroSerializationUtils)
    api(projects.swissknifeAvroSchemaCatalogueDomain)
    api(projects.swissknifeCryptographyDomain)

    implementation(projects.swissknifeKotlinExtensions)
    implementation(projects.acmeSchemaCatalogueAvroCommon)

    testImplementation(projects.swissknifeTestUtils)
    testImplementation(projects.pillarAvroSerializationTestUtils)
    testImplementation(projects.swissknifeAvroSchemaCatalogueTestUtils)
    testImplementation(projects.swissknifeCryptographyImplementationBouncyCastle)
}