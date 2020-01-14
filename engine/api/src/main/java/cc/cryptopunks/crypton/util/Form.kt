package cc.cryptopunks.crypton.util

data class Form(
    val fields: Map<Field.Id, Field> = emptyMap()
) {
    constructor(vararg fields: Field) : this(fields.map { it.id to it }.toMap())

    object Submit
    data class Error(val message: String?)

    operator fun <F: Field> Field.Id.invoke() = fields[this] as? F

    fun setField(field: Field) = copy(
        fields = fields + (field.id to field)
    )

    val Field.Id.text get() = fields[this] as Field.Text
}

interface Field {
    val id: Id
    interface Id

    interface Text : CharSequence
}

data class TextField(
    override val id: Field.Id,
    val text: CharSequence
) : Field,
    Field.Text,
    CharSequence by text {

    val string get() = text.toString()
}

