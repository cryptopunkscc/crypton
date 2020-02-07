package cc.cryptopunks.crypton.util

data class TextField(
    override val id: Form.Field.Id,
    val text: CharSequence
) : Form.Field,
    Form.Field.Text,
    CharSequence by text {

    val string get() = text.toString()
}