plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.acmeConventions)
    api(libs.swissknife.messaging.domain)

    testImplementation(libs.swissknife.test.utils)
}