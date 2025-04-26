package sollecitom.libs.pillar.avro.serialization.correlation.core.access.authentication

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.deserializeWith
import sollecitom.libs.swissknife.avro.serialization.utils.getRecordFromUnion
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
        setRecordInUnion(value.type(), record)
    }

    override fun deserialize(value: GenericRecord) = value.getRecordFromUnion { unionTypeName, unionRecord ->
        when (unionTypeName) {
            Types.credentialsBased -> unionRecord.deserializeWith(CredentialsBasedAuthentication.avroSerde)
            Types.federated -> unionRecord.deserializeWith(FederatedAuthentication.avroSerde)
            Types.stateless -> unionRecord.deserializeWith(StatelessAuthentication.avroSerde)
            else -> error("Unknown authentication type $unionTypeName")
        }
    }

    private fun Authentication.type(): String = when (this) {
        is CredentialsBasedAuthentication -> Types.credentialsBased
        is FederatedAuthentication -> Types.federated
        is StatelessAuthentication -> Types.stateless
    }

    private object Types {
        const val credentialsBased = "credentialsBased"
        const val federated = "federated"
        const val stateless = "stateless"
    }
}
