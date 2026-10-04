package sollecitom.libs.pillar.avro.serialization.correlation.core.access

import sollecitom.libs.swissknife.avro.schema.catalogue.test.utils.SchemaContainerTestSpecification
import assertk.assertThat
import assertk.assertions.containsExactlyInAnyOrder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class AccessAvroSchemasTests : SchemaContainerTestSpecification {

    override val candidate = AccessAvroSchemas

    @Test
    fun `lists every schema in its namespace`() {

        val schemaNames = candidate.all.map { it.name }.toList()

        assertThat(schemaNames).containsExactlyInAnyOrder("UnauthenticatedAccess", "AuthenticatedAccess", "Access")
    }
}