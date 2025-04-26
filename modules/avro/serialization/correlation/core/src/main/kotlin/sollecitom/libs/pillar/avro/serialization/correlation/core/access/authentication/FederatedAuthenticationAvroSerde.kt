package sollecitom.libs.pillar.avro.serialization.correlation.core.access.authentication

import sollecitom.libs.pillar.avro.serialization.correlation.core.access.session.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.FederatedAuthentication
import sollecitom.libs.swissknife.correlation.core.domain.access.session.FederatedSession
import org.apache.avro.generic.GenericRecord

val FederatedAuthentication.Companion.avroSchema get() = AuthenticationAvroSchemas.federatedAuthentication
val FederatedAuthentication.Companion.avroSerde: AvroSerde<FederatedAuthentication> get() = FederatedAuthenticationAvroSerde

private object FederatedAuthenticationAvroSerde : AvroSerde<FederatedAuthentication> {

    override val schema get() = FederatedAuthentication.avroSchema

    override fun serialize(value: FederatedAuthentication): GenericRecord = buildRecord {

        setValue(Fields.token, value.token, Authentication.Token.avroSerde)
        setValue(Fields.session, value.session, FederatedSession.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val token = getValue(Fields.token, Authentication.Token.avroSerde)
        val session = getValue(Fields.session, FederatedSession.avroSerde)
        FederatedAuthentication(token = token, session = session)
    }

    private object Fields {
        const val token = "token"
        const val session = "session"
    }
}