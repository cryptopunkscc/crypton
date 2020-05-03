package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import android.widget.EditText
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.service.AccountForm.*
import cc.cryptopunks.crypton.service.CreateAccountService
import cc.cryptopunks.crypton.util.Form
import cc.cryptopunks.crypton.util.TextField
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.bindings.textChanges
import cc.cryptopunks.crypton.widget.ActorLayout
import kotlinx.android.synthetic.main.create_account.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class CreateAccountView(context: Context) : ActorLayout(context) {

    private lateinit var action: CreateAccountService.Action

    init {
        View.inflate(
            context,
            R.layout.create_account,
            this
        )
    }

    private val formFields: Map<Form.Field.Id<TextField>, EditText>
        get() = mapOf(
            ServiceName to serviceName,
            UserName to userName,
            Password to password,
            ConfirmPassword to confirmPassword
        )

    override fun Connector.connect(): Job = launch {
        launch {
            input.collect { arg ->
                when (arg) {
                    is Form -> setForm(arg)
                    is Error -> errorOutput.text = arg.message
                }
            }
        }
        launch {
            flowOf(
                registerButton.clicks().map { action },
                formFields.textFieldChanges().map { Form.SetField(it) }
            )
                .flattenMerge()
                .collect(output)
        }
    }

    private fun setForm(form: Form) {
        form.fields
            .mapKeys { (id, _) -> formFields[id] }
            .map { (view, text) -> view?.setText(text as Form.Field.Text) }
    }


    private fun Map<Form.Field.Id<TextField>, EditText>.textFieldChanges() = map { (id, editText) ->
        editText.textChanges().map {
            TextField(id, it)
        }
    }.asFlow().flattenMerge()

    fun login() = apply {
        action = CreateAccountService.Login
        registerButton.text = "Sing in"
        confirmPasswordLayout.visibility = View.GONE
    }

    fun register() = apply {
        action = CreateAccountService.Register
        registerButton.text = "Sing up"
        confirmPasswordLayout.visibility = View.VISIBLE
    }
}
