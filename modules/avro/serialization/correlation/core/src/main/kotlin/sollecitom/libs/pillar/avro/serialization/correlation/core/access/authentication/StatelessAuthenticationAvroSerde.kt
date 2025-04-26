package sollecitom.libs.pillar.avro.serialization.correlation.core.access.authentication

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.StatelessAuthentication
import org.apache.avro.generic.GenericRecord

val StatelessAuthentication.Companion.avroSchema get() = AuthenticationAvroSchemas.statelessAuthentication
val StatelessAuthentication.Companion.avroSerde: AvroSerde<StatelessAuthentication> get() = StatelessAuthenticationAvroSerde

private object StatelessAuthenticationAvroSerde : AvroSerde<StatelessAuthentication> {

    override val schema get() = StatelessAuthentication.avroSchema

    override fun serialize(value: StatelessAuthentication): GenericRecord = buildRecord {

        setValue(Fields.token, value.token, Authentication.Token.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val token = getValue(Fields.token, Authentication.Token.avroSerde)
        StatelessAuthentication(token = token)
    }

    private object Fields {
        const val token = "token"
    }
}