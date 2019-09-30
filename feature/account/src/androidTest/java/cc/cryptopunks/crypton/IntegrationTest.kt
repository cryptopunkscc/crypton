package cc.cryptopunks.crypton

import androidx.test.core.app.ApplicationProvider
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.smack.integration.ApiIntegrationTest
import org.junit.After
import org.junit.Before
import java.util.concurrent.atomic.AtomicReference

abstract class IntegrationTest : ApiIntegrationTest() {

    init {
        smackFactory
    }

    private val componentRef = AtomicReference<IntegrationTestComponent>()

    internal val component: IntegrationTestComponent
        get() = componentRef.run {
            get() ?: DaggerIntegrationTestComponent.builder()
                .testModule(TestModule(ApplicationProvider.getApplicationContext()))
                .build()
                .also { set(it) }
        }

    @Before
    override fun setUp() {
        super.setUp()
    }

    @After
    override fun tearDown() {
        super.tearDown()
        component.database.clearAllTables()
        componentRef.set(null)
    }

    fun account(index: Long, withId: Boolean = false) = createAccount(
        config(index)
    ).run {
        if (!withId) this
        else copy(address = index)
    }

    fun Client.insertAccount(
        reduce: Account.() -> Account = { this }
    ) = account(accountId).reduce().run {
        copy(address = component.accountRepo.insert(this)!!)
    }

}

fun createAccount(config: Client.Config) = Account(
    address = config.address,
    password = config.password
)