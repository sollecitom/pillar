dependencies {
    api(projects.swissknifeDddDomain)

    implementation(projects.acmeSchemaCatalogueJsonCommon)
    implementation(projects.pillarJsonSerializationCorrelationCore)

    testImplementation(projects.pillarJsonSerializationTestUtils)
    testImplementation(projects.swissknifeCoreTestUtils)
    testImplementation(projects.swissknifeCorrelationCoreTestUtils)
    testImplementation(projects.swissknifeTestUtils)
}