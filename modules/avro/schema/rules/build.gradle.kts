dependencies {
    api(projects.swissknifeAvroSchemaChecker)

    implementation(projects.swissknifeKotlinExtensions)

    testImplementation(projects.pillarAvroSerializationTestUtils)
    testImplementation(projects.swissknifeResourceUtils)
    testImplementation(projects.swissknifeLoggingStandardSlf4jConfiguration)
    testImplementation(projects.swissknifeComplianceCheckerTestUtils)
}