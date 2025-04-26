dependencies {
    api(projects.swissknifeCoreDomain)
    api(projects.swissknifeJsonUtils)

    implementation(projects.swissknifeKotlinExtensions)
    implementation(projects.acmeSchemataJsonCommon)

    testImplementation(projects.pillarJsonSerializationTestUtils)
    testImplementation(projects.swissknifeCoreTestUtils)
    testImplementation(projects.swissknifeTestUtils)
}