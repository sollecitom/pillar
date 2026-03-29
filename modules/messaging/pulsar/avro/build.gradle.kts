plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.messagingAvro)
    api(libs.swissknife.pulsar.messaging.adapter)
    api(libs.swissknife.pulsar.utils)
    api(libs.swissknife.pulsar.avro.utils)
}