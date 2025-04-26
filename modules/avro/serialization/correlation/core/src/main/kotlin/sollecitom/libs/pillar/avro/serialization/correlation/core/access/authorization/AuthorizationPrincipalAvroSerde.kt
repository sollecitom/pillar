package sollecitom.libs.pillar.avro.serialization.correlation.core.access.authorization

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.AuthorizationPrincipal
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.Roles
import org.apache.avro.generic.GenericRecord

val AuthorizationPrincipal.Companion.avroSchema get() = AuthorizationAvroSchemas.authorizationPrincipal
val AuthorizationPrincipal.Companion.avroSerde: AvroSerde<AuthorizationPrincipal> get() = AuthorizationPrincipalAvroSerde

private object AuthorizationPrincipalAvroSerde : AvroSerde<AuthorizationPrincipal> {

    override val schema get() = AuthorizationPrincipal.avroSchema

    override fun serialize(value: AuthorizationPrincipal): GenericRecord = buildRecord {

        setValue(Fields.roles, value.roles, Roles.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val roles = getValue(Fields.roles, Roles.avroSerde)
        AuthorizationPrincipal(roles = roles)
    }

    private object Fields {
        const val roles = "roles"
    }
}