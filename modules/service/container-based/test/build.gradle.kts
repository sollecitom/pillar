plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.acmeConventions)
    api(libs.test.containers.junit.jupiter)

    implementation(projects.serviceLogging)
}