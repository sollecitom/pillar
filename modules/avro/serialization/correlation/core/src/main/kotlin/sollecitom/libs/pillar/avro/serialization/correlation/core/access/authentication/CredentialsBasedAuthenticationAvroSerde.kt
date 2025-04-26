package sollecitom.libs.pillar.avro.serialization.correlation.core.access.authentication

import sollecitom.libs.pillar.avro.serialization.correlation.core.access.session.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.CredentialsBasedAuthentication
import sollecitom.libs.swissknife.correlation.core.domain.access.session.SimpleSession
import org.apache.avro.generic.GenericRecord

val CredentialsBasedAuthentication.Companion.avroSchema get() = AuthenticationAvroSchemas.credentialsBasedAuthentication
val CredentialsBasedAuthentication.Companion.avroSerde: AvroSerde<CredentialsBasedAuthentication> get() = CredentialsBasedAuthenticationAvroSerde

private object CredentialsBasedAuthenticationAvroSerde : AvroSerde<CredentialsBasedAuthentication> {

    override val schema get() = CredentialsBasedAuthentication.avroSchema

    override fun serialize(value: CredentialsBasedAuthentication): GenericRecord = buildRecord {

        setValue(Fields.token, value.token, Authentication.Token.avroSerde)
        setValue(Fields.session, value.session, SimpleSession.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val token = getValue(Fields.token, Authentication.Token.avroSerde)
        val session = getValue(Fields.session, SimpleSession.avroSerde)
        CredentialsBasedAuthentication(token = token, session = session)
    }

    private object Fields {
        const val token = "token"
        const val session = "session"
    }
}