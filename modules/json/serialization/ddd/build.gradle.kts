dependencies {
    api(projects.swissknifeDddDomain)

    implementation(projects.acmeSchemataJsonCommon)
    implementation(projects.pillarJsonSerializationCorrelationCore)

    testImplementation(projects.pillarJsonSerializationTestUtils)
    testImplementation(projects.swissknifeCoreTestUtils)
    testImplementation(projects.swissknifeCorrelationCoreTestUtils)
    testImplementation(projects.swissknifeTestUtils)
}