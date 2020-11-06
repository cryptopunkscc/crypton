package cc.cryptopunks.crypton.fs.util

private val hexArray = "0123456789ABCDEF".toCharArray()

fun ByteArray.toHex(): String {
    val hexChars = CharArray(size * 2)
    for (index in indices) {
        val v = (get(index).toInt() and 0xFF)

        hexChars[index * 2] = hexArray[v ushr 4]
        hexChars[index * 2 + 1] = hexArray[v and 0x0F]
    }
    return String(hexChars)
}
