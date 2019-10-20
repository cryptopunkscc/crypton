package cc.cryptopunks.crypton.smack.util

import cc.cryptopunks.crypton.net.Net
import cc.cryptopunks.crypton.smack.net.account.getErrorMessage
import org.jivesoftware.smack.sasl.SASLErrorException
import org.jxmpp.stringprep.XmppStringprepException

object MapClientException : (Throwable) -> Throwable? by { throwable ->
    when(throwable) {
        is SASLErrorException -> throwable.getErrorMessage()
        is XmppStringprepException -> throwable.localizedMessage
        else -> null
    }?.let { message ->
        Net.Exception(
            message = message,
            cause = throwable
        )
    }
}