dependencies {
    api(projects.swissknifeJsonUtils)

    implementation(projects.swissknifeKotlinExtensions)
    implementation(projects.swissknifeJsonUtils)

    testImplementation(projects.swissknifeJsonTestUtils)
    testImplementation(projects.swissknifeKotlinExtensions)
    testImplementation(projects.swissknifeResourceUtils)
    testImplementation(projects.swissknifeLoggingStandardSlf4jConfiguration)
}