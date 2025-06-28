dependencies {
    api(projects.jwtDomain)

    testImplementation(libs.swissknife.test.utils)
    testImplementation(libs.swissknife.jwt.test.utils)
    testImplementation(libs.swissknife.core.test.utils)
    testImplementation(projects.jwtTestUtils)
    testImplementation(libs.swissknife.jwt.jose4j.processor)
    testImplementation(libs.swissknife.jwt.jose4j.issuer)
}