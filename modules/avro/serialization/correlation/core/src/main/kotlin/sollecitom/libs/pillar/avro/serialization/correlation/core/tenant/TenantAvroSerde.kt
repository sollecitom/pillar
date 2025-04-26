package sollecitom.libs.pillar.avro.serialization.correlation.core.tenant

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import org.apache.avro.generic.GenericRecord

val Tenant.Companion.avroSchema get() = TenantAvroSchemas.tenant
val Tenant.Companion.avroSerde: AvroSerde<Tenant> get() = TenantAvroSerde

private object TenantAvroSerde : AvroSerde<Tenant> {

    override val schema get() = Tenant.avroSchema

    override fun serialize(value: Tenant): GenericRecord = buildRecord {

        setValue(Fields.id, value.id, Id.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val id = getValue(Fields.id, Id.avroSerde)
        Tenant(id = id)
    }

    private object Fields {
        const val id = "id"
    }
}