dependencies {
    api(projects.pillarJsonSerializationCore)
    api(projects.swissknifeWebApiDomain)

    implementation(projects.acmeSchemaCatalogueJsonCommon)
    implementation(projects.swissknifeKotlinExtensions)

    testImplementation(projects.pillarJsonSerializationTestUtils)
    testImplementation(projects.swissknifeCorrelationCoreTestUtils)
    testImplementation(projects.swissknifeCoreTestUtils)
    testImplementation(projects.swissknifeTestUtils)
}
