package sollecitom.libs.pillar.avro.serialization.correlation.core.access.session

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSerde
import sollecitom.libs.pillar.avro.serialization.correlation.core.access.idp.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.idp.IdentityProvider
import sollecitom.libs.swissknife.correlation.core.domain.access.session.FederatedSession
import org.apache.avro.generic.GenericRecord

val FederatedSession.Companion.avroSchema get() = SessionAvroSchemas.federatedSession
val FederatedSession.Companion.avroSerde: AvroSerde<FederatedSession> get() = FederatedSessionAvroSerde

private object FederatedSessionAvroSerde : AvroSerde<FederatedSession> {

    override val schema get() = FederatedSession.avroSchema

    override fun serialize(value: FederatedSession): GenericRecord = buildRecord {

        setValue(Fields.id, value.id, Id.avroSerde)
        setValue(Fields.identityProvider, value.identityProvider, IdentityProvider.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val id = getValue(Fields.id, Id.avroSerde)
        val identityProvider = getValue(Fields.identityProvider, IdentityProvider.avroSerde)
        FederatedSession(id = id, identityProvider = identityProvider)
    }

    private object Fields {
        const val id = "id"
        const val identityProvider = "identity_provider"
    }
}