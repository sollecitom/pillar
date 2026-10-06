plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.webApiUtils)

    implementation(libs.guava)
    implementation(libs.swissknife.lens.correlation.extensions)
    implementation(libs.swissknife.web.client.info.analyzer)
    implementation(libs.swissknife.jwt.jose4j.processor)
    implementation(projects.jwtDomain)
}
