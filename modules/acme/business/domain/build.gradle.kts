plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(libs.swissknife.core.utils)
    api(libs.swissknife.correlation.core.domain)
    api(libs.swissknife.web.api.utils)
    api(projects.acmeConventions)
    api(projects.messagingConventions)
    api(projects.httpApiConventions)

    testImplementation(libs.swissknife.test.utils)
}