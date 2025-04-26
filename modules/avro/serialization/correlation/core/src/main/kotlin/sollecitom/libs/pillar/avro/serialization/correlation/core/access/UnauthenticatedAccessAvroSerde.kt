package sollecitom.libs.pillar.avro.serialization.correlation.core.access

import sollecitom.libs.pillar.avro.serialization.correlation.core.access.authorization.avroSerde
import sollecitom.libs.pillar.avro.serialization.correlation.core.access.origin.avroSerde
import sollecitom.libs.pillar.avro.serialization.correlation.core.access.scope.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.*
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.AuthorizationPrincipal
import sollecitom.libs.swissknife.correlation.core.domain.access.origin.Origin
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessScope
import org.apache.avro.generic.GenericRecord

val Access.Unauthenticated.Companion.avroSchema get() = AccessAvroSchemas.unauthenticatedAccess
val Access.Unauthenticated.Companion.avroSerde: AvroSerde<Access.Unauthenticated> get() = UnauthenticatedAccessAvroSerde

private object UnauthenticatedAccessAvroSerde : AvroSerde<Access.Unauthenticated> {

    override val schema get() = Access.Unauthenticated.avroSchema

    override fun serialize(value: Access.Unauthenticated): GenericRecord = buildRecord {

        setValue(Fields.origin, value.origin, Origin.avroSerde)
        setValue(Fields.authorization, value.authorization, AuthorizationPrincipal.avroSerde)
        setValue(Fields.scope, value.scope, AccessScope.avroSerde)
        set(Fields.isTest, value.isTest)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val origin = getValue(Fields.origin, Origin.avroSerde)
        val authorization = getValue(Fields.authorization, AuthorizationPrincipal.avroSerde)
        val scope = getValue(Fields.scope, AccessScope.avroSerde)
        val isTest = getBoolean(Fields.isTest)
        Access.Unauthenticated(origin = origin, authorization = authorization, scope = scope, isTest = isTest)
    }

    private object Fields {
        const val origin = "origin"
        const val authorization = "authorization"
        const val scope = "scope"
        const val isTest = "is_test"
    }
}