package cc.cryptopunks.crypton

data class Transformation<I, O>(
    val strategy: String,
    val inputType: Class<I>,
    val outputType: Class<O>,
)

inline fun <reified I, reified O> transformation(strategy: String) =
    Transformation(strategy, I::class.java, O::class.java)

fun <I, O> Transformation<I, O>.flip() =
    Transformation(strategy, outputType, inputType)

