dependencies {
    api(projects.swissknifePaginationDomain)
    api(projects.swissknifeJsonUtils)
    api(projects.pillarJsonSerializationCore)

    implementation(projects.acmeSchemaCatalogueJsonCommon)
    implementation(projects.swissknifeKotlinExtensions)

    testImplementation(projects.pillarJsonSerializationTestUtils)
    testImplementation(projects.swissknifePaginationTestUtils)
    testImplementation(projects.swissknifeCoreTestUtils)
    testImplementation(projects.swissknifeTestUtils)
}