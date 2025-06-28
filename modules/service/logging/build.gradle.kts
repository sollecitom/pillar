dependencies {
    api(projects.acmeConventions)
    api(libs.swissknife.logger.core)

    runtimeOnly(libs.swissknife.logging.standard.slf4j.configuration)
}