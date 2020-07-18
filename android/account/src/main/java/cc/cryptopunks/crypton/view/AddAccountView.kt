package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import android.widget.EditText
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Password
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.bindings.textChanges
import cc.cryptopunks.crypton.widget.ActorLayout
import kotlinx.android.synthetic.main.create_account.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
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
                addButton.clicks().clearError().map { Account.Service.Add(account()) },
                registerButton.clicks().clearError().map { Account.Service.Register(account()) }
            )
                .flattenMerge()
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


    private fun Map<Account.Field, EditText>.textFieldChanges() = map { (field, editText) ->
        editText.textChanges().map { text ->
            field to text
        }
    }.asFlow().flattenMerge()

    private fun Flow<Unit>.clearError() = onEach { errorOutput.text = null  }
}
