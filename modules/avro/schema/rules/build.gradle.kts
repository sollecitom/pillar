plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(libs.swissknife.avro.schema.checker)

    implementation(libs.swissknife.kotlin.extensions)

    testImplementation(projects.avroSerializationTestUtils)
    testImplementation(libs.swissknife.resource.utils)
    testImplementation(libs.swissknife.logging.standard.slf4j.configuration)
    testImplementation(libs.swissknife.compliance.checker.test.utils)
}