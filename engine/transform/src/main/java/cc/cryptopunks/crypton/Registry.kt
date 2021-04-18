package cc.cryptopunks.crypton


fun <I, O> transform(transformation: Transformation<I, O>): Transform<I, O> =
    requireNotNull(serializers[transformation] as? Transform<I, O>) {
        """
Cannot find transformation for 
$transformation 
Available transformations:
${availableTransformations()}
        """.trimIndent()
    }


infix fun <I, O> Transformation<I, O>.register(transform: Transform<I, O>) = apply {
    serializers[this] = transform
}


private val serializers = mutableMapOf<Transformation<*, *>, Transform<*, *>>()

private fun availableTransformations() = serializers.keys.joinToString("\n")
