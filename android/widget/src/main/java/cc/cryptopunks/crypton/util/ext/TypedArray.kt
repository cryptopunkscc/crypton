package cc.cryptopunks.crypton.util.ext

import android.content.res.TypedArray
import android.util.TypedValue
import androidx.annotation.StyleableRes

internal fun TypedArray.obtainString(@StyleableRes index: Int) = getType(index).let { type ->
    when (type) {
        TypedValue.TYPE_STRING -> getString(index)
        TypedValue.TYPE_REFERENCE -> getResourceId(index, 0)
            .takeIf { it == 0 }
            ?.let { resources.getString(it) }
        else -> error("Cannot resolve string for TypedValue TYPE: $type")
    }
}