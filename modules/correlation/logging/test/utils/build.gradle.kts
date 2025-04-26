dependencies {
    api(projects.pillarCorrelationLoggingUtils)
    api(projects.swissknifeCorrelationCoreDomain)
    api(projects.swissknifeJsonTestUtils)
    api(projects.swissknifeCorrelationCoreTestUtils)
    api(projects.swissknifeCoreTestUtils)
    api(projects.swissknifeTestUtils)

    implementation(projects.pillarJsonSerializationCorrelationCore)
    implementation(projects.swissknifeKotlinExtensions)
}