dependencies {
    api(projects.pillarJwtDomain)

    testImplementation(projects.swissknifeTestUtils)
    testImplementation(projects.swissknifeJwtTestUtils)
    testImplementation(projects.swissknifeCoreTestUtils)
    testImplementation(projects.pillarJwtTestUtils)
    testImplementation(projects.swissknifeJwtJose4jProcessor)
    testImplementation(projects.swissknifeJwtJose4jIssuer)
}