plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.jwtDomain)
    api(libs.swissknife.test.utils)
    api(libs.swissknife.jwt.test.utils)
}