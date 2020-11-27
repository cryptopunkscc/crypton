package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Upload
import kotlinx.coroutines.flow.Flow
import java.io.File

class MockUploadNet : Upload.Net {
    override fun upload(file: File): Flow<Upload.Progress> {
        TODO("Not yet implemented")
    }
}
