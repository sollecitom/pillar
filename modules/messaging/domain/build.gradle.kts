dependencies {
    api(libs.swissknife.messaging.domain)

    implementation(projects.correlationLoggingUtils)

    testImplementation(libs.swissknife.ddd.test.utils)
    testImplementation(projects.messagingTestUtils)
    testImplementation(libs.swissknife.logging.standard.slf4j.configuration)
}