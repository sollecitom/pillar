plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.avroSchemaRules)
    api(libs.swissknife.avro.serialization.test.utils)
}