package sollecitom.libs.pillar.avro.serialization.correlation.core.access.authentication

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.deserializeWith
import sollecitom.libs.swissknife.avro.serialization.utils.getEnvelope
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.CredentialsBasedAuthentication
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.FederatedAuthentication
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.StatelessAuthentication
import org.apache.avro.generic.GenericRecord

val Authentication.Companion.avroSchema get() = AuthenticationAvroSchemas.authentication
val Authentication.Companion.avroSerde: AvroSerde<Authentication> get() = AuthenticationAvroSerde

private object AuthenticationAvroSerde : AvroSerde<Authentication> {

    override val schema get() = Authentication.avroSchema

    override fun serialize(value: Authentication): GenericRecord = buildRecord {
        val record = when (value) {
            is CredentialsBasedAuthentication -> CredentialsBasedAuthentication.avroSerde.serialize(value)
            is FederatedAuthentication -> FederatedAuthentication.avroSerde.serialize(value)
            is StatelessAuthentication -> StatelessAuthentication.avroSerde.serialize(value)
        }
        setEnvelope(record)
    }

    override fun deserialize(value: GenericRecord) = value.getEnvelope { branchName, envelope ->
        when (branchName) {
            CredentialsBasedAuthentication.avroSerde.schema.name -> envelope.deserializeWith(CredentialsBasedAuthentication.avroSerde)
            FederatedAuthentication.avroSerde.schema.name -> envelope.deserializeWith(FederatedAuthentication.avroSerde)
            StatelessAuthentication.avroSerde.schema.name -> envelope.deserializeWith(StatelessAuthentication.avroSerde)
            else -> error("Unknown authentication type $branchName")
        }
    }
}
