package sollecitom.libs.pillar.avro.serialization.correlation.core.toggles

import sollecitom.libs.pillar.avro.serialization.core.identity.IdentityAvroSchemas
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import org.apache.avro.Schema

object TogglesAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.correlation.toggles") {

    val booleanToggleValue by lazy { getSchema(name = "BooleanToggleValue", dependencies = setOf(IdentityAvroSchemas.id)) }
    val integerToggleValue by lazy { getSchema(name = "IntegerToggleValue", dependencies = setOf(IdentityAvroSchemas.id)) }
    val decimalToggleValue by lazy { getSchema(name = "DecimalToggleValue", dependencies = setOf(IdentityAvroSchemas.id)) }
    val enumToggleValue by lazy { getSchema(name = "EnumToggleValue", dependencies = setOf(IdentityAvroSchemas.id)) }
    val toggleValue by lazy { getSchema(name = "ToggleValue", dependencies = setOf(booleanToggleValue, integerToggleValue, decimalToggleValue, enumToggleValue)) }
    val toggles by lazy { getSchema(name = "Toggles", dependencies = setOf(toggleValue)) }

    override val nestedContainers: Set<AvroSchemaContainer> = emptySet()

    override val all: Sequence<Schema> = sequenceOf(booleanToggleValue, integerToggleValue, decimalToggleValue, enumToggleValue, toggleValue, toggles)
}