plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.messagingConventions)
    api(libs.swissknife.avro.serialization.utils)
}