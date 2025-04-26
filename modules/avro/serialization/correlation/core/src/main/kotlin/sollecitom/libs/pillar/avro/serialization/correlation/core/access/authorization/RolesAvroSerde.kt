package sollecitom.libs.pillar.avro.serialization.correlation.core.access.authorization

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValues
import sollecitom.libs.swissknife.avro.serialization.utils.setValues
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.Role
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.Roles
import org.apache.avro.generic.GenericRecord

val Roles.Companion.avroSchema get() = AuthorizationAvroSchemas.roles
val Roles.Companion.avroSerde: AvroSerde<Roles> get() = RolesAvroSerde

private object RolesAvroSerde : AvroSerde<Roles> {

    override val schema get() = Roles.avroSchema

    override fun serialize(value: Roles): GenericRecord = buildRecord {

        setValues(Fields.values, value.values.toList(), Role.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val values = getValues(Fields.values, Role.avroSerde).toSet()
        Roles(values = values)
    }

    private object Fields {
        const val values = "values"
    }
}