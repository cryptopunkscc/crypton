package cc.cryptopunks.crypton.smack.util

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.smack.api.account.getErrorMessage
import org.jivesoftware.smack.sasl.SASLErrorException
import org.jxmpp.stringprep.XmppStringprepException

object MapClientException : (Throwable) -> Throwable? by { throwable ->
    when(throwable) {
        is SASLErrorException -> throwable.getErrorMessage()
        is XmppStringprepException -> throwable.localizedMessage
        else -> null
    }?.let { message ->
        Api.Exception(
            message = message,
            cause = throwable
        )
    }
}