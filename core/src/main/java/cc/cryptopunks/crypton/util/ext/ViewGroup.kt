package cc.cryptopunks.crypton.util.ext

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(
    @LayoutRes layout: Int,
    attachToParent: Boolean = false
): View = LayoutInflater
    .from(context)
    .inflate(layout, this, attachToParent)