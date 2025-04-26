package sollecitom.libs.pillar.avro.serialization.correlation.core.access.origin.client.info

import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import org.apache.avro.Schema

object ClientInfoAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.correlation.access.origin.client.info") {

    val device by lazy { getSchema(name = "Device") }
    val layoutEngine by lazy { getSchema(name = "LayoutEngine") }
    val operatingSystem by lazy { getSchema(name = "OperatingSystem") }
    val agent by lazy { getSchema(name = "Agent") }
    val clientInfo by lazy { getSchema(name = "ClientInfo", dependencies = setOf(device, layoutEngine, operatingSystem, agent)) }

    override val nestedContainers: Set<AvroSchemaContainer> = emptySet()

    override val all: Sequence<Schema> = sequenceOf(device, layoutEngine, operatingSystem, agent, clientInfo)
}