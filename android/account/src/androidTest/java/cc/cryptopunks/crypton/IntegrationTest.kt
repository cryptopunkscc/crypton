package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.smack.integration.ApiIntegrationTest
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import java.util.concurrent.atomic.AtomicReference

abstract class IntegrationTest : ApiIntegrationTest() {

    private val componentRef = AtomicReference<IntegrationTestComponent>()

    internal val component: IntegrationTestComponent
        get() = componentRef.run {
            get() ?: createComponent().also { set(it) }
        }

    private fun createComponent(): IntegrationTestComponent = TODO()

    @Before
    override fun setUp() {
        super.setUp()
    }

    @After
    override fun tearDown() {
        super.tearDown()
        component.clearDatabase()
        componentRef.set(null)
    }

    fun address(id: Int) = Address("$id@test.io")

    fun account(address: Address): Account = createAccount(
        config(address.local.toLong())
    )

    fun Api.insertAccount(
        reduce: Account.() -> Account = { this }
    ) = launch {
        account(address).reduce().run {
            copy(address = component.accountRepo.insert(this).address)
        }
    }

}

fun createAccount(config: Api.Config) = Account(
    address = config.address,
    password = config.password
)