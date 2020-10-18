package cc.cryptopunks.crypton.smack.util

import cc.cryptopunks.crypton.context.Net
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
fun SASLErrorException.getErrorMessage() = saslFailure.saslError.toString().replace("_", " ")
