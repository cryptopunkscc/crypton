package cc.cryptopunks.crypton

class TypedByteArray<T>(
    val type: Class<T>,
    val bytes: ByteArray
)

class TypedString<T>(
    val type: Class<T>,
    val string: String
)
