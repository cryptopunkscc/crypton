package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import android.widget.EditText
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.bindings.textChanges
import cc.cryptopunks.crypton.widget.ActorLayout
import kotlinx.android.synthetic.main.create_account.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class CreateAccountView(context: Context) : ActorLayout(context) {

    private lateinit var action: Any

    init {
        View.inflate(
            context,
            R.layout.create_account,
            this
        )
    }

    private val formFields: Map<Account.Field, EditText>
        get() = mapOf(
            Account.Field.ServiceName to serviceName,
            Account.Field.UserName to userName,
            Account.Field.Password to password,
            Account.Field.ConfirmPassword to confirmPassword
        )

    override fun Connector.connect(): Job = launch {
        launch {
            input.collect { arg ->
                when (arg) {
                    is Map<*, *> -> setForm(arg as Map<Account.Field, CharSequence>)
                    is Error -> errorOutput.text = arg.message
                }
            }
        }
        launch {
            flowOf(
                registerButton.clicks().map { action },
                formFields.textFieldChanges().map { (field, text) ->
                    Account.Service.Set(field, text)
                }
            )
                .flattenMerge()
                .collect(output)
        }
    }

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

    fun login() = apply {
        action = Account.Service.Add()
        registerButton.text = "Sing in"
        confirmPasswordLayout.visibility = View.GONE
    }

    fun register() = apply {
        action = Account.Service.Register()
        registerButton.text = "Sing up"
        confirmPasswordLayout.visibility = View.VISIBLE
    }
}
