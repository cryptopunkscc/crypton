package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Route
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

class CommandsTest {

    @Test
    fun `add account`() {
        assertEquals(
            listOf(
                Account.Service.Set(Account.Field.UserName, "test@account.io"),
                Account.Service.Set(Account.Field.Password, "pass"),
                Account.Service.Login()
            ),
            Context(Route.SetAccount)
                .prepare()
                .set("add account test@account.io password pass")
                .execute()
        )
    }

    @Test
    fun `create account`() {
        assertEquals(
            listOf(
                Account.Service.Set(Account.Field.UserName, "test@account.io"),
                Account.Service.Set(Account.Field.Password, "pass"),
                Account.Service.Register()
            ),
            Context(Route.SetAccount)
                .prepare()
                .set("create account test@account.io password pass")
                .execute()
        )
    }

    @Test
    fun `send message`() {
        assertEquals(
            Chat.Service.SendMessage("lorem ipsum"),
            Context(Route.Chat().apply {
                chatAddress = "chat@address.io"
                accountId = "test@account.io"
            })
                .prepare()
                .set("send message lorem ipsum")
                .execute()
        )
    }

    @Test
    fun `navigate roster`() {
        assertEquals(
            Route.Roster,
            Context()
                .prepare()
                .set("navigate roster")
                .execute()
        )
    }

    @Test
    fun `navigate create chat`() {
        assertEquals(
            Route.CreateChat(),
            Context()
                .prepare()
                .set("navigate create chat")
                .execute()
        )
    }

    @Test
    @Ignore // TODO
    fun `navigate chat`() {
        assertEquals(
            Route.CreateChat(),
            Context()
                .prepare()
                .set("navigate chat")
                .execute()
        )
    }
}
