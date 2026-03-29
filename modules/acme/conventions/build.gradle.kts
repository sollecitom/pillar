plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(libs.swissknife.core.domain)
    api(libs.swissknife.messaging.domain)
    api(libs.swissknife.web.api.utils)
}