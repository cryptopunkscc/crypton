package cc.cryptopunks.crypton.viewmodel

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.Input
import cc.cryptopunks.crypton.util.cache
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountFromViewModel @Inject constructor(
    private val broadcastError: BroadcastError
) {

    val serviceName = Input().cache()
    val userName = Input().cache()
    val password = Input().cache()
    val confirmPassword = Input().cache()
    val onClick = 0L.cache()
    val errorMessage = "".cache()

    val address
        get() = Address(
            local = userName.value.text,
            domain = serviceName.value.text
        )
    val account
        get() = Account(
            address = address,
            password = password.value.text
        )

    suspend operator fun invoke() = coroutineScope {
        launch {
            onClick.filter { it > 0 }.collect {
                errorMessage("")
            }
        }
        launch {
            broadcastError
                .mapNotNull { (it as? Account.Exception)?.cause?.localizedMessage }
                .collect { errorMessage(it) }
        }
    }
}