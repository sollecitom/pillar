dependencies {
    api(libs.swissknife.json.utils)

    implementation(libs.swissknife.kotlin.extensions)
    implementation(libs.swissknife.json.utils)

    testImplementation(libs.swissknife.json.test.utils)
    testImplementation(libs.swissknife.kotlin.extensions)
    testImplementation(libs.swissknife.resource.utils)
    testImplementation(libs.swissknife.logging.standard.slf4j.configuration)
}