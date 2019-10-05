package cc.cryptopunks.crypton.feature.account.viewmodel

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.Input
import cc.cryptopunks.kache.core.Kache
import cc.cryptopunks.kache.core.KacheManager
import cc.cryptopunks.kache.core.lazy
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountViewModel @Inject constructor(
    private val broadcastError: BroadcastError
) : Kache.Provider by KacheManager() {

    val serviceName by lazy<Input>("serviceName")
    val userName by lazy<Input>("userName")
    val password by lazy<Input>("password")
    val confirmPassword by lazy<Input>("confirmPassword")
    val onClick by lazy("loginButton", initial = 0L)
    val errorMessage by lazy("errorMessage", initial = "")
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
            onClick.asFlow().filter { it > 0 }.collect {
                errorMessage.value = ""
            }
        }
        launch {
            broadcastError
                .mapNotNull { (it as? Account.Exception)?.cause?.localizedMessage }
                .collect { errorMessage(it) }
        }
    }
}