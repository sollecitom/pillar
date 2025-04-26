package sollecitom.libs.pillar.avro.serialization.correlation.core.access.scope

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValues
import sollecitom.libs.swissknife.avro.serialization.utils.setValues
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessContainer
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessScope
import org.apache.avro.generic.GenericRecord

val AccessScope.Companion.avroSchema get() = AccessScopeAvroSchemas.scope
val AccessScope.Companion.avroSerde: AvroSerde<AccessScope> get() = AccessScopeAvroSerde

private object AccessScopeAvroSerde : AvroSerde<AccessScope> {

    override val schema get() = AccessScope.avroSchema

    override fun serialize(value: AccessScope): GenericRecord = buildRecord {

        setValues(Fields.containerStack, value.containers, AccessContainer.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val containerStack = getValues(Fields.containerStack, AccessContainer.avroSerde)
        AccessScope(containerStack = containerStack)
    }

    private object Fields {
        const val containerStack = "container_stack"
    }
}