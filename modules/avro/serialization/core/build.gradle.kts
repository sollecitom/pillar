dependencies {
    api(projects.swissknifeCoreDomain)
    api(projects.swissknifeAvroSerializationUtils)
    api(projects.swissknifeAvroSchemaCatalogueDomain)

    api(projects.acmeSchemaCatalogueAvroCommon)

    implementation(projects.swissknifeKotlinExtensions)

    testImplementation(projects.pillarAvroSerializationTestUtils)
    testImplementation(projects.swissknifeAvroSchemaCatalogueTestUtils)
    testImplementation(projects.swissknifeCoreTestUtils)
    testImplementation(projects.swissknifeTestUtils)
}