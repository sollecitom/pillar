dependencies {
    api(projects.swissknifeDddDomain)
    api(projects.swissknifeAvroSerializationUtils)
    api(projects.swissknifeAvroSchemaCatalogueDomain)

    implementation(projects.pillarAvroSerializationCorrelationCore)
    implementation(projects.pillarAvroSerializationCore)
    implementation(projects.swissknifeKotlinExtensions)

    testImplementation(projects.pillarAvroSerializationTestUtils)
    testImplementation(projects.swissknifeDddTestUtils)
    testImplementation(projects.swissknifeAvroSchemaCatalogueTestUtils)
    testImplementation(projects.swissknifeTestUtils)
}