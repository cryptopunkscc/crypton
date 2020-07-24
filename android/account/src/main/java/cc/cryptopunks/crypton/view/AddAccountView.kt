package cc.cryptopunks.crypton.view

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.EditText
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Password
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.widget.ActorLayout
import kotlinx.android.synthetic.main.create_account.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class AddAccountView(context: Context) : ActorLayout(context) {

    init {
        View.inflate(context, R.layout.create_account, this)
    }

    private val formFields: Map<Account.Field, EditText>
        get() = mapOf(
            Account.Field.ServiceName to serviceName,
            Account.Field.UserName to userName,
            Account.Field.Password to password,
            Account.Field.ConfirmPassword to confirmPassword
        )

    override fun Connector.connect(): Job = launch {
        val navigateUpOnConnected = findNavController().previousBackStackEntry?.destination !is NavGraph
        launch {
            input.collect { arg ->
                when (arg) {
                    is Map<*, *> -> setForm(arg as Map<Account.Field, CharSequence>)
                    is Account.Service.Error -> errorOutput.text = arg.message
                    is Account.Service.Connected -> if (navigateUpOnConnected) findNavController().navigateUp()
                }
            }
        }
        launch {
            flowOf(
                addButton.clicks().map { Exec.Login(account()) },
                registerButton.clicks().map { Exec.Register(account()) }
            )
                .flattenMerge()
                .clearError()
                .collect(output)
        }
    }

    private fun account() = formFields.run {
        Account(
            address = Address(Account.Field.UserName.text, Account.Field.ServiceName.text),
            password = Password(Account.Field.Password.text)
        )
    }

    private val Account.Field.text get() = formFields.getValue(this).text.toString()

    private fun setForm(form: Map<Account.Field, CharSequence>) {
        form
            .mapKeys { (id, _) -> formFields[id] }
            .map { (view, text) -> view?.setText(text) }
    }

    private fun <T> Flow<T>.clearError() = onEach { errorOutput.text = null  }
}
