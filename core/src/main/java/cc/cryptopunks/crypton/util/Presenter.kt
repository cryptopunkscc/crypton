package cc.cryptopunks.crypton.util

interface Presenter<View> {
    suspend operator fun invoke(): Any = Unit
    suspend operator fun View.invoke(): Any = Unit
}

suspend operator fun <View> Presenter<View>.invoke(view: View) = view()