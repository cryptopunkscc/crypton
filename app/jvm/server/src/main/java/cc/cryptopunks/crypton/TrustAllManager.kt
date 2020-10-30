package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.logger.typedLog
import java.security.GeneralSecurityException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

object TrustAllManager : X509TrustManager {
    private val log = typedLog()
    override fun checkClientTrusted(
        certs: Array<X509Certificate>,
        authType: String
    ) = Unit

    override fun checkServerTrusted(
        certs: Array<X509Certificate>,
        authType: String
    ) = Unit

    override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()

    fun install() = try {
        SSLContext.getInstance("SSL").run {
            init(null, arrayOf(TrustAllManager), SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory)
        }
        log.d { "Installed" }
    } catch (e: GeneralSecurityException) {
        e.printStackTrace()
    }
}
