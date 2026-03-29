plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.messagingJson)
    api(libs.swissknife.pulsar.messaging.adapter)
    api(libs.swissknife.pulsar.utils)
    api(libs.swissknife.pulsar.json.utils)
    api(libs.swissknife.pulsar.json.serialization)
}