package cc.cryptopunks.crypton.smack.module

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Api
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Connection
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Upload
import cc.cryptopunks.crypton.smack.SmackCore
import cc.cryptopunks.crypton.smack.net.account.AccountNet
import cc.cryptopunks.crypton.smack.net.api.NetEventBroadcast
import cc.cryptopunks.crypton.smack.net.chat.ChatNet
import cc.cryptopunks.crypton.smack.net.file.UploadNet
import cc.cryptopunks.crypton.smack.net.message.MessageNet
import cc.cryptopunks.crypton.smack.net.omemo.DeviceNet
import cc.cryptopunks.crypton.smack.net.omemo.InitOmemo
import cc.cryptopunks.crypton.smack.net.roster.RosterNet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration


internal fun createConnection(
    scope: CoroutineScope,
    account: Address,
    configuration: XMPPTCPConnectionConfiguration,
) = ConnectionModule(
    scope = scope,
    account = account,
    smack = SmackModule(
        configuration = configuration,
        account = account,
        scope = scope
    )
)

internal class SmackNet(
    scope: CoroutineScope,
    smack: SmackCore,
) : SmackCore by smack, Net {

    private val initOmemo by lazy { InitOmemo(omemoManager) }

    private val netEvents by lazy {
        NetEventBroadcast(
            scope = scope,
            connection = connection,
            initOmemo = initOmemo
        )
    }

    override fun netEvents(): Flow<Api.Event> = netEvents.flow()

    override fun isConnected(): Boolean = connection.isConnected

    override suspend fun initOmemo() = initOmemo.invoke()
    override fun isOmemoInitialized(): Boolean = initOmemo.isInitialized

    override fun connect() {
        configuration
        connection.run {
            if (!isConnected)
                connect()
        }
    }

    override fun disconnect() {
        connection.disconnect()
    }


    override fun interrupt() {
        connection.instantShutdown()
    }
}

internal class ConnectionModule(
    scope: CoroutineScope,
    private val account: Address,
    private val smack: SmackCore,
    override val net: Net = SmackNet(scope, smack),
    override val deviceNet: DeviceNet = DeviceNet(smack),
    override val accountNet: Account.Net = AccountNet(smack),
    override val messageNet: Message.Net = MessageNet(smack),
    override val chatNet: Chat.Net = ChatNet(smack, account),
    override val rosterNet: Roster.Net = RosterNet(smack),
    override val uploadNet: Upload.Net = UploadNet(smack),
) : SmackCore by smack,
    Connection {

    init {
        omemoManager.setTrustCallback(deviceNet)
    }
}

private fun <T> SmackCore.lazy(init: SmackCore.() -> T) = kotlin.lazy { init() }
