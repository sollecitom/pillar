dependencies {
    api(projects.swissknifeCorrelationCoreDomain)
    api(projects.swissknifeJsonUtils)
    api(projects.pillarJsonSerializationCore)

    implementation(projects.swissknifeKotlinExtensions)
    implementation(projects.acmeSchemaCatalogueJsonCommon)

    testImplementation(projects.pillarJsonSerializationTestUtils)
    testImplementation(projects.swissknifeCorrelationCoreTestUtils)
    testImplementation(projects.swissknifeCoreTestUtils)
    testImplementation(projects.swissknifeTestUtils)
}