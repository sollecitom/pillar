package sollecitom.libs.pillar.avro.serialization.correlation.core.access.scope

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessContainer
import org.apache.avro.generic.GenericRecord

val AccessContainer.Companion.avroSchema get() = AccessScopeAvroSchemas.container
val AccessContainer.Companion.avroSerde: AvroSerde<AccessContainer> get() = AccessContainerAvroSerde

private object AccessContainerAvroSerde : AvroSerde<AccessContainer> {

    override val schema get() = AccessContainer.avroSchema

    override fun serialize(value: AccessContainer): GenericRecord = buildRecord {

        setValue(Fields.id, value.id, Id.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val id = getValue(Fields.id, Id.avroSerde)
        AccessContainer(id = id)
    }

    private object Fields {
        const val id = "id"
    }
}