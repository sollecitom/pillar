dependencies {
    api(libs.swissknife.openapi.checking.checker)

    implementation(libs.swissknife.kotlin.extensions)
    implementation(libs.swissknife.json.utils)

    testImplementation(libs.swissknife.openapi.checking.test.utils)
    testImplementation(libs.swissknife.kotlin.extensions)
    testImplementation(libs.swissknife.resource.utils)
    testImplementation(libs.swissknife.logging.standard.slf4j.configuration)
}