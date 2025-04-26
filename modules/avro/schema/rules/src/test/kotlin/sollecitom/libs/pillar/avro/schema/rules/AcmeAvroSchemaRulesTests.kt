package sollecitom.libs.pillar.avro.schema.rules

import sollecitom.libs.pillar.avro.schema.rules.specs.FieldNamesTestSpecification
import sollecitom.libs.pillar.avro.schema.rules.specs.NamespaceTestSpecification
import sollecitom.libs.pillar.avro.schema.rules.specs.SchemaNameTestSpecification
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class AcmeAvroSchemaRulesTests {

    @Nested
    inner class SchemaName : SchemaNameTestSpecification {

        override val rules get() = AcmeAvroSchemaRules
    }

    @Nested
    inner class Namespace : NamespaceTestSpecification {

        override val rules get() = AcmeAvroSchemaRules
    }

    @Nested
    inner class FieldNames : FieldNamesTestSpecification {

        override val rules get() = AcmeAvroSchemaRules
    }
}