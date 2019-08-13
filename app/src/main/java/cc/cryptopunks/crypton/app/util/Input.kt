package cc.cryptopunks.crypton.app.util

import android.content.SharedPreferences
import cc.cryptopunks.kache.android.SharedPreferencesPersistent
import cc.cryptopunks.kache.core.Kache
import cc.cryptopunks.kache.core.invoke

data class Input(
    val text: String = "",
    val error: String = ""
) : SharedPreferencesPersistent {

    override fun SharedPreferences.load(): SharedPreferencesPersistent? = Input(
        text = getString(TEXT_KEY, "")!!,
        error = getString(ERROR_KEY, "")!!
    )

    override fun SharedPreferences.Editor.save() {
        putString(TEXT_KEY, text)
        putString(ERROR_KEY, error)
    }

    companion object {
        private const val TEXT_KEY = "text"
        private const val ERROR_KEY = "error"

        val EMPTY = Input()
    }
}

var Kache<Input>.text: String
    get() = value.text
    set(value) = invoke { copy(text = value) }