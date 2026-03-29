plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.jsonSchemaRules)
    api(libs.swissknife.json.test.utils)
}