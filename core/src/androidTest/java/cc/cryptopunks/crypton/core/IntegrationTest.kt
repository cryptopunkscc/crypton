package cc.cryptopunks.crypton.core

import androidx.test.core.app.ApplicationProvider
import cc.cryptopunks.crypton.core.entity.Account
import cc.cryptopunks.crypton.xmpp.Xmpp
import cc.cryptopunks.crypton.smack.integration.XmppIntegrationTest
import org.junit.After
import org.junit.Before
import java.util.concurrent.atomic.AtomicReference

abstract class IntegrationTest : XmppIntegrationTest() {

    init {
        smackFactory
    }

    private val componentRef = AtomicReference<TestComponent>()

    internal val component: TestComponent
        get() = componentRef.run {
            get() ?: DaggerTestComponent.builder()
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

    fun account(index: Long, withId: Boolean = false) = Account(config(index)).run {
        if (!withId) this
        else copy(id = index)
    }

    fun Xmpp.insertAccount(
        reduce: Account.() -> Account = { this }
    ) = account(id).reduce().run {
        copy(id = component.accountDao.insert(this)!!)
    }

}