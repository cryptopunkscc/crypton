package cc.cryptopunks.crypton.sys

import cc.cryptopunks.crypton.aesgcm.aesDecrypt
import cc.cryptopunks.crypton.aesgcm.aesEncrypt
import cc.cryptopunks.crypton.context.AesGcm
import java.io.InputStream
import java.io.OutputStream

object JvmAesGcmSys : AesGcm.Sys {

    override fun encrypt(
        stream: InputStream,
        iv: ByteArray,
        key: ByteArray
    ) = stream.aesEncrypt(iv, key)

    override fun decrypt(
        stream: OutputStream,
        iv: ByteArray,
        key: ByteArray
    ) = stream.aesDecrypt(iv, key)
}
