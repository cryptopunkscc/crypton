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
        config(
            index
        )
    ).run {
        if (!withId) this
        else copy(id = index)
    }

    fun Client.insertAccount(
        reduce: Account.() -> Account = { this }
    ) = account(id).reduce().run {
        copy(id = component.accountDao.insert(this)!!)
    }

}

fun createAccount(config: Client.Config) = Account(
    domain = config.remoteId.domain,
    credentials = Account.Credentials(
        userName = config.remoteId.local,
        password = config.password
    )
)