package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.smack.client.getErrorMessage
import org.jivesoftware.smack.sasl.SASLErrorException
import org.jxmpp.stringprep.XmppStringprepException

object MapClientException : (Throwable) -> Throwable? by { throwable ->
    when(throwable) {
        is SASLErrorException -> throwable.getErrorMessage()
        is XmppStringprepException -> throwable.localizedMessage
        else -> null
    }?.let { message ->
        Client.Exception(
            message = message,
            cause = throwable
        )
    }
}