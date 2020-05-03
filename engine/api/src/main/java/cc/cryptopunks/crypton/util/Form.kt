package cc.cryptopunks.crypton.util

import cc.cryptopunks.crypton.context.Connectable

data class Form(
    val fields: Map<Field.Id<*>, Field> = emptyMap()
) {
    interface Field {
        val id: Id<*>

        interface Id<F: Field>
        interface Text : CharSequence
    }

    data class Set(val form: Form) : Service.Input
    data class SetField(val field: Field) : Service.Input

    interface Service : Connectable {
        interface Input
        interface Output
    }

    fun setField(field: Field) = copy(
        fields = fields + (field.id to field)
    )

    fun <F: Field> Field.Id<F>.get() = fields[this] as F
}
