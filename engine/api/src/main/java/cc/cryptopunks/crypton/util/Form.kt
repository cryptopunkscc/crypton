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
    object Submit : Service.Input

    data class Error(val message: String?) : Service.Output

    interface Service : Connectable {
        interface Input
        interface Output
    }

    constructor(vararg fields: Field) : this(fields.map { it.id to it }.toMap())

    fun setField(field: Field) = copy(
        fields = fields + (field.id to field)
    )

    fun <F: Field> Field.Id<F>.get() = fields[this] as F
}