dependencies {
    api(projects.swissknifeCorrelationCoreDomain)
    api(projects.swissknifeAvroSerializationUtils)
    api(projects.swissknifeAvroSchemaCatalogueDomain)

    implementation(projects.pillarAvroSerializationCore)
    implementation(projects.swissknifeKotlinExtensions)

    testImplementation(projects.pillarAvroSerializationTestUtils)
    testImplementation(projects.swissknifeCorrelationCoreTestUtils)
    testImplementation(projects.swissknifeAvroSchemaCatalogueTestUtils)
    testImplementation(projects.swissknifeTestUtils)
}