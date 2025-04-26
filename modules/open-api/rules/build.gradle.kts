dependencies {
    api(projects.swissknifeOpenapiCheckingChecker)

    implementation(projects.swissknifeKotlinExtensions)
    implementation(projects.swissknifeJsonUtils)

    testImplementation(projects.swissknifeOpenapiCheckingTestUtils)
    testImplementation(projects.swissknifeKotlinExtensions)
    testImplementation(projects.swissknifeResourceUtils)
    testImplementation(projects.swissknifeLoggingStandardSlf4jConfiguration)
}