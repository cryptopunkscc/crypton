package cc.cryptopunks.crypton

typealias Transform<I, O> = I.() -> O

fun <I, O> I.map(transformation: Transformation<I, O>): O =
    transform(transformation)(this)
