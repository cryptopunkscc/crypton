package cc.cryptopunks.crypton.sys

import cc.cryptopunks.crypton.aesgcm.withAes
import cc.cryptopunks.crypton.context.AesGcm
import cc.cryptopunks.crypton.context.Crypto
import java.io.InputStream
import java.io.OutputStream
import java.util.*

object JvmCryptoSys : Crypto.Sys {

    override fun encodeBase64(
        array: ByteArray
    ) = Base64.getEncoder().encodeToString(array)!!

    override fun decodeBase64(
        string: String
    ) = Base64.getDecoder().decode(string)!!

    override fun encrypt(
        stream: InputStream,
        secure: AesGcm.Secure
    ) = stream.withAes(secure.iv, secure.key)

    override fun decrypt(
        stream: OutputStream,
        secure: AesGcm.Secure
    ) = stream.withAes(secure.iv, secure.key)
}
