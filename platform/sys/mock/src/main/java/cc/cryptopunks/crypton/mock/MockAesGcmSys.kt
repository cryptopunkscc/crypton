package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.AesGcm
import java.io.InputStream
import java.io.OutputStream

object MockAesGcmSys : AesGcm.Sys {
    override fun encrypt(inputStream: InputStream, iv: ByteArray, key: ByteArray): InputStream {
        TODO("Not yet implemented")
    }

    override fun decrypt(outputStream: OutputStream, iv: ByteArray, key: ByteArray): OutputStream {
        TODO("Not yet implemented")
    }
}
