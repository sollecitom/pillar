package sollecitom.libs.pillar.avro.serialization.correlation.core.access.session

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.session.SimpleSession
import org.apache.avro.generic.GenericRecord

val SimpleSession.Companion.avroSchema get() = SessionAvroSchemas.simpleSession
val SimpleSession.Companion.avroSerde: AvroSerde<SimpleSession> get() = SimpleSessionAvroSerde

private object SimpleSessionAvroSerde : AvroSerde<SimpleSession> {

    override val schema get() = SimpleSession.avroSchema

    override fun serialize(value: SimpleSession): GenericRecord = buildRecord {

        setValue(Fields.id, value.id, Id.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val id = getValue(Fields.id, Id.avroSerde)
        SimpleSession(id = id)
    }

    private object Fields {
        const val id = "id"
    }
}