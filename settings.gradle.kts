@file:Suppress("UnstableApiUsage")

includeBuild(".")
includeBuild("gradle-plugins")
includeProject("swissknife")
includeProject("acme-schema-catalogue")

rootProject.name = "pillar"

fun module(vararg pathSegments: String) = subProject(rootFolder = "modules", pathSegments = pathSegments)

fun subProject(rootFolder: String, vararg pathSegments: String, excludeRootFolderFromGroupName: Boolean = true) {

    val projectName = pathSegments.last()
    val path = listOf(rootFolder) + pathSegments.dropLast(1)
    val group = if (excludeRootFolderFromGroupName) path.minus(rootFolder).joinToString(separator = "-", prefix = "${rootProject.name}-") else path.joinToString(separator = "-", prefix = "${rootProject.name}-")
    val directory = path.joinToString(separator = "/", prefix = "./")
    val fullProjectName = "${if (group.isEmpty()) "" else "$group-"}$projectName"

    include(fullProjectName)
    project(":$fullProjectName").projectDir = mkdir("$directory/$projectName")
}

fun includeProject(name: String) {

    apply("$name/settings.gradle.kts")
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

module("acme", "conventions")
module("correlation", "logging", "utils")
module("correlation", "logging", "test", "utils")
module("prometheus", "micrometer", "registry")
module("service", "logging")
module("service", "container-based", "test")
module("http", "api", "conventions")
module("messaging", "domain")
module("messaging", "conventions")
module("messaging", "avro")
module("messaging", "json")
module("messaging", "test", "utils")
module("messaging", "pulsar", "avro")
module("messaging", "pulsar", "json")
module("encryption", "messaging", "serialization", "avro")
module("protected-value", "domain")
module("protected-value", "messaging", "serialization", "avro")
module("jwt", "domain")
module("jwt", "parsing", "tests")
module("jwt", "test", "utils")
module("web", "api", "utils")
module("web", "api", "test", "utils")
module("open-api", "rules")
module("avro", "schema", "rules")
module("avro", "serialization", "test", "utils")
module("avro", "serialization", "core")
module("avro", "serialization", "correlation", "core")
module("avro", "serialization", "ddd")
module("json", "schema", "rules")
module("json", "serialization", "test", "utils")
module("json", "serialization", "core")
module("json", "serialization", "correlation", "core")
module("json", "serialization", "ddd")
module("json", "serialization", "pagination")
module("json", "serialization", "web", "api")
module("acme", "business", "domain")