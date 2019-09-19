package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.module.ViewModelScope
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.Input
import cc.cryptopunks.crypton.util.ViewModel
import cc.cryptopunks.kache.core.Kache
import cc.cryptopunks.kache.core.KacheManager
import cc.cryptopunks.kache.core.lazy
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import org.jivesoftware.smack.sasl.SASLErrorException
import org.jxmpp.stringprep.XmppStringprepException
import javax.inject.Inject

@ViewModelScope
class AccountViewModel @Inject constructor(
    private val broadcastError: BroadcastError
) : ViewModel,
    Kache.Provider by KacheManager() {

    val serviceName by lazy<Input>("serviceName")
    val userName by lazy<Input>("userName")
    val password by lazy<Input>("password")
    val confirmPassword by lazy<Input>("confirmPassword")
    val onClick by lazy("loginButton", initial = 0L)
    val errorMessage by lazy("errorMessage", initial = "")

    fun getAccount() = Account(
        domain = serviceName.value.text,
        credentials = Account.Credentials(
            userName = userName.value.text,
            password = password.value.text
        )
    )

    suspend operator fun invoke() = coroutineScope {
        launch {
            broadcastError
                .mapNotNull { (it as? Account.Exception)?.cause }
                .collect { throwable ->
                    errorMessage(
                        when (throwable) {
                            is SASLErrorException -> throwable.getErrorMessage()
                            is XmppStringprepException -> throwable.localizedMessage
                            else -> throwable.localizedMessage
                        }
                    )
                }
        }
        launch {
            onClick.asFlow().filter { it > 0 }.collect {
                errorMessage.value = ""
            }
        }
    }
}

fun SASLErrorException.getErrorMessage() = saslFailure.saslError.toString().replace("_", " ")