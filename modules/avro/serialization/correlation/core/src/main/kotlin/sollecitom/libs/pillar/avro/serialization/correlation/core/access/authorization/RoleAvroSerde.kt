package sollecitom.libs.pillar.avro.serialization.correlation.core.access.authorization

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getString
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.Role
import org.apache.avro.generic.GenericRecord

val Role.Companion.avroSchema get() = AuthorizationAvroSchemas.role
val Role.Companion.avroSerde: AvroSerde<Role> get() = RoleAvroSerde

private object RoleAvroSerde : AvroSerde<Role> {

    override val schema get() = Role.avroSchema

    override fun serialize(value: Role): GenericRecord = buildRecord {

        set(Fields.name, value.name.value)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val name = getString(Fields.name).let(::Name)
        Role(name = name)
    }

    private object Fields {
        const val name = "name"
    }
}