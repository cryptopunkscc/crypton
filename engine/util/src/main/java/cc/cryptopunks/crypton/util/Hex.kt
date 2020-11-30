package cc.cryptopunks.crypton.util

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

fun String.hexToBytes(): ByteArray {
    val array = ByteArray(length / 2)
    var i = 0
    while (i < length) {
        array[i / 2] = ((Character.digit(this[i], 16) shl 4) +
            Character.digit(this[i + 1], 16)).toByte()
        i += 2
    }
    return array
}
