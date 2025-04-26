package sollecitom.libs.pillar.json.serialization.correlation.core.access.actor

import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.ActorOnBehalf
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.DirectActor
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.ImpersonatingActor
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import org.json.JSONObject

private object ActorJsonSerde : JsonSerde.SchemaAware<Actor> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/actor/Actor.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Actor) = when (value) {
        is DirectActor -> DirectActor.jsonSerde.serialize(value)
        is ActorOnBehalf -> ActorOnBehalf.jsonSerde.serialize(value)
        is ImpersonatingActor -> ImpersonatingActor.jsonSerde.serialize(value)
    }

    override fun deserialize(value: JSONObject) = when (val type = value.getRequiredString(Fields.TYPE)) {
        DirectActorJsonSerde.TYPE_VALUE -> DirectActor.jsonSerde.deserialize(value)
        ActorOnBehalfJsonSerde.TYPE_VALUE -> ActorOnBehalf.jsonSerde.deserialize(value)
        ImpersonatingActorJsonSerde.TYPE_VALUE -> ImpersonatingActor.jsonSerde.deserialize(value)
        else -> error("Unsupported actor type $type")
    }

    private object Fields {
        const val TYPE = "type"
    }
}

val Actor.Companion.jsonSerde: JsonSerde.SchemaAware<Actor> get() = ActorJsonSerde