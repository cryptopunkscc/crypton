package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.AesGcm
import cc.cryptopunks.crypton.context.Crypto
import java.io.InputStream
import java.io.OutputStream

object MockCryptoSys : Crypto.Sys {
    override fun encodeBase64(array: ByteArray): String {
        TODO("Not yet implemented")
    }

    override fun decodeBase64(string: String): ByteArray {
        TODO("Not yet implemented")
    }

    override fun transform(
        stream: InputStream,
        secure: AesGcm.Secure,
        mode: Crypto.Mode
    ): InputStream {
        TODO("Not yet implemented")
    }

    override fun transform(
        stream: OutputStream,
        secure: AesGcm.Secure,
        mode: Crypto.Mode
    ): OutputStream {
        TODO("Not yet implemented")
    }
}
