package cc.cryptopunks.crypton.repo.test

import cc.cryptopunks.crypton.context.Device
import cc.cryptopunks.crypton.context.address
import cc.cryptopunks.crypton.repo.DeviceRepo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

abstract class DeviceRepoTest {

    abstract val repo: DeviceRepo

    @Test
    fun setGetClear() = runBlocking {
        // given
        val value = "value"
        val fingerprint = Device.Fingerprint(
            value = value,
            device = Device(
                id = 1,
                address = address("test@address")
            )
        )
        repo.run {
            // when
            set(fingerprint)

            // then
            assertEquals(fingerprint, get(value))

            // when
            clear()

            // then
            assertNull(get(value))
        }
    }
}
