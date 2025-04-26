dependencies {
    api(projects.swissknifeMessagingDomain)

    implementation(projects.pillarCorrelationLoggingUtils)

    testImplementation(projects.swissknifeDddTestUtils)
    testImplementation(projects.pillarMessagingTestUtils)
    testImplementation(projects.swissknifeLoggingStandardSlf4jConfiguration)
}