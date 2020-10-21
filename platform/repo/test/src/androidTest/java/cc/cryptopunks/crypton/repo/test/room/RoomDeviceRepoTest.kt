package cc.cryptopunks.crypton.repo.test.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import cc.cryptopunks.crypton.repo.DeviceRepo
import cc.cryptopunks.crypton.repo.test.DeviceRepoTest
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomDeviceRepoTest : DeviceRepoTest() {
    private val db = roomTestDatabase(InstrumentationRegistry.getInstrumentation().context)
    override val repo = DeviceRepo(db.fingerprintDao)
}
