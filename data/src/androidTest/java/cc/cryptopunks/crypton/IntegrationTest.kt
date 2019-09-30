package cc.cryptopunks.crypton

import androidx.test.core.app.ApplicationProvider
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Address
import org.junit.After
import java.util.concurrent.atomic.AtomicReference

abstract class IntegrationTest {

    private val baseId = Address(domain = "test.io")

    private val componentRef = AtomicReference<IntegrationTestComponent>()

    internal val component: IntegrationTestComponent
        get() = componentRef.run {
            get() ?: DaggerIntegrationTestComponent.builder()
                .testModule(TestModule(ApplicationProvider.getApplicationContext()))
                .build()
                .also { set(it) }
        }


    @After
    fun tearDown() {
        component.database.clearAllTables()
        componentRef.set(null)
    }

    fun account(index: Long, withId: Boolean = false) = baseId.copy(
        local = "test$index"
    ).run {
        Account(
            address = Address(
                local = local,
                domain = domain
            ),
            password = local
        )
    }

}