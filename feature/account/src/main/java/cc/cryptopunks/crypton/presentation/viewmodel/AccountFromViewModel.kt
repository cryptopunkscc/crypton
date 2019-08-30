package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.util.HandleError
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.module.ViewModelScope
import cc.cryptopunks.crypton.util.Input
import cc.cryptopunks.crypton.util.ViewModel
import cc.cryptopunks.kache.core.Kache
import cc.cryptopunks.kache.core.KacheManager
import cc.cryptopunks.kache.core.lazy
import cc.cryptopunks.kache.rxjava.observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.jivesoftware.smack.sasl.SASLErrorException
import org.jxmpp.stringprep.XmppStringprepException
import javax.inject.Inject

@ViewModelScope
class AccountViewModel @Inject constructor(
    private val errorPublisher: HandleError.Publisher
) : () -> Disposable,
    ViewModel,
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

    override fun invoke(): Disposable = CompositeDisposable(
        errorPublisher.observable()
            .filter { it is Account.Exception }
            .map { it.cause!! }
            .subscribe { throwable ->
                errorMessage(
                    when (throwable) {
                        is SASLErrorException -> throwable.getErrorMessage()
                        is XmppStringprepException -> throwable.localizedMessage
                        else -> throwable.localizedMessage
                    }
                )
            },
        onClick.observable().filter { it > 0 }.subscribe {
            errorMessage.value = ""
        }
    )
}

fun SASLErrorException.getErrorMessage() = saslFailure.saslError.toString().replace("_", " ")