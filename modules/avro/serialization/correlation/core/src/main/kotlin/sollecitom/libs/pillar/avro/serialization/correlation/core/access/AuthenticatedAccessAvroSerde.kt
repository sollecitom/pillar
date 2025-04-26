package sollecitom.libs.pillar.avro.serialization.correlation.core.access

import sollecitom.libs.pillar.avro.serialization.correlation.core.access.actor.avroSerde
import sollecitom.libs.pillar.avro.serialization.correlation.core.access.authorization.avroSerde
import sollecitom.libs.pillar.avro.serialization.correlation.core.access.origin.avroSerde
import sollecitom.libs.pillar.avro.serialization.correlation.core.access.scope.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.AuthorizationPrincipal
import sollecitom.libs.swissknife.correlation.core.domain.access.origin.Origin
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessScope
import org.apache.avro.generic.GenericRecord

val Access.Authenticated.Companion.avroSchema get() = AccessAvroSchemas.authenticatedAccess
val Access.Authenticated.Companion.avroSerde: AvroSerde<Access.Authenticated> get() = AuthenticatedAccessAvroSerde

private object AuthenticatedAccessAvroSerde : AvroSerde<Access.Authenticated> {

    override val schema get() = Access.Authenticated.avroSchema

    override fun serialize(value: Access.Authenticated): GenericRecord = buildRecord {

        setValue(Fields.actor, value.actor, Actor.avroSerde)
        setValue(Fields.origin, value.origin, Origin.avroSerde)
        setValue(Fields.authorization, value.authorization, AuthorizationPrincipal.avroSerde)
        setValue(Fields.scope, value.scope, AccessScope.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val actor = getValue(Fields.actor, Actor.avroSerde)
        val origin = getValue(Fields.origin, Origin.avroSerde)
        val authorization = getValue(Fields.authorization, AuthorizationPrincipal.avroSerde)
        val scope = getValue(Fields.scope, AccessScope.avroSerde)
        Access.Authenticated(actor = actor, origin = origin, authorization = authorization, scope = scope)
    }

    private object Fields {
        const val actor = "actor"
        const val origin = "origin"
        const val authorization = "authorization"
        const val scope = "scope"
    }
}