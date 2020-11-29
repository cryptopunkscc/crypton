package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.AesGcm
import java.io.InputStream
import java.io.OutputStream

object MockAesGcmSys : AesGcm.Sys {
    override fun encrypt(inputStream: InputStream, secure: AesGcm.Secure): InputStream {
        TODO("Not yet implemented")
    }

    override fun decrypt(outputStream: OutputStream, secure: AesGcm.Secure): OutputStream {
        TODO("Not yet implemented")
    }
}
