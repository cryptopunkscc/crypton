package cc.cryptopunks.crypton.sys

import cc.cryptopunks.crypton.aesgcm.withAes
import cc.cryptopunks.crypton.context.AesGcm
import java.io.InputStream
import java.io.OutputStream

object AndroidAesGcmSys : AesGcm.Sys {

    override fun encrypt(
        stream: InputStream,
        secure: AesGcm.Secure
    ) = stream.withAes(secure.iv, secure.key)

    override fun decrypt(
        stream: OutputStream,
        secure: AesGcm.Secure
    ) = stream.withAes(secure.iv, secure.key)
}
