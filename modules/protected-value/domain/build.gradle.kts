dependencies {
    api(projects.swissknifeCryptographyDomain)
    api(projects.swissknifeProtectedValueDomain)

    implementation(projects.swissknifeKotlinExtensions)

    testImplementation(projects.swissknifeTestUtils)
}