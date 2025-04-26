dependencies {
    api(projects.swissknifeCoreDomain)
    api(projects.swissknifeJsonUtils)

    implementation(projects.swissknifeKotlinExtensions)
    implementation(projects.acmeSchemaCatalogueJsonCommon)

    testImplementation(projects.pillarJsonSerializationTestUtils)
    testImplementation(projects.swissknifeCoreTestUtils)
    testImplementation(projects.swissknifeTestUtils)
}