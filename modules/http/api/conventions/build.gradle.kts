plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.acmeConventions)
    api(libs.swissknife.web.api.utils)

    testImplementation(libs.swissknife.test.utils)
}