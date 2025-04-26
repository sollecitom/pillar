package sollecitom.libs.pillar.avro.serialization.correlation.core.access.authentication

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSerde
import sollecitom.libs.pillar.avro.serialization.core.time.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.*
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import kotlinx.datetime.Instant
import org.apache.avro.generic.GenericRecord

val Authentication.Token.Companion.avroSchema get() = AuthenticationAvroSchemas.authenticationToken
val Authentication.Token.Companion.avroSerde: AvroSerde<Authentication.Token> get() = AuthenticationTokenAvroSerde

private object AuthenticationTokenAvroSerde : AvroSerde<Authentication.Token> {

    override val schema get() = Authentication.Token.avroSchema

    override fun serialize(value: Authentication.Token): GenericRecord = buildRecord {

        setValue(Fields.id, value.id, Id.avroSerde)
        setValueOrNull(Fields.validFrom, value.validFrom, Instant.avroSerde)
        setValueOrNull(Fields.validTo, value.validTo, Instant.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val id = getValue(Fields.id, Id.avroSerde)
        val validFrom = getValueOrNull(Fields.validFrom, Instant.avroSerde)
        val validTo = getValueOrNull(Fields.validTo, Instant.avroSerde)
        Authentication.Token(id = id, validFrom = validFrom, validTo = validTo)
    }

    private object Fields {
        const val id = "id"
        const val validFrom = "valid_from"
        const val validTo = "valid_to"
    }
}