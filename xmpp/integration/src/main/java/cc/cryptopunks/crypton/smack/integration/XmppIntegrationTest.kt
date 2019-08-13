package cc.cryptopunks.crypton.smack.integration

import cc.cryptopunks.crypton.xmpp.Xmpp
import cc.cryptopunks.crypton.xmpp.entities.Jid
import cc.cryptopunks.crypton.smack.SmackXmppFactory

abstract class XmppIntegrationTest {

    val baseJid = Jid(domain = "test.io")
    private val xmpps = mutableMapOf<Long, Xmpp>()
    private var autoRemove = false

    val xmpp1 get() = xmpp(1)
    val xmpp2 get() = xmpp(2)
    val xmpp3 get() = xmpp(3)

    open fun init() {}

    open fun setUp() {
        init()
        autoRemove = xmpps.isNotEmpty()
        xmpps.values.forEach {
            with(it) {
                create()
                login()
            }
        }
    }

    open fun tearDown() {
        if (autoRemove) xmpps.values.forEach {
            with(it) {
                remove()
            }
        }
        xmpps.clear()
    }

    fun xmpps(range: IntRange) = range.map {
        xmpp(it.toLong())
    }

    fun xmpp(index: Long) = synchronized(xmpps) {
        xmpps.getOrElse(index) {
            createXmpp(config(index)).also {
                xmpps[index] = it
            }
        }
    }

    fun config(index: Long) = Xmpp.Config(
        id = index,
        jid = baseJid.copy(local = "test$index"),
        password = "test$index"
    )
}

internal typealias createXmpp = SmackXmppFactory