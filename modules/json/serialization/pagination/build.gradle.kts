dependencies {
    api(projects.swissknifePaginationDomain)
    api(projects.swissknifeJsonUtils)
    api(projects.pillarJsonSerializationCore)

    implementation(projects.acmeSchemataJsonCommon)
    implementation(projects.swissknifeKotlinExtensions)

    testImplementation(projects.pillarJsonSerializationTestUtils)
    testImplementation(projects.swissknifePaginationTestUtils)
    testImplementation(projects.swissknifeCoreTestUtils)
    testImplementation(projects.swissknifeTestUtils)
}