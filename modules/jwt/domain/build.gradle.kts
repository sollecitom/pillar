plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(libs.swissknife.core.utils)
    api(libs.swissknife.jwt.domain)
    api(libs.swissknife.json.utils)
    api(projects.acmeBusinessDomain)
}