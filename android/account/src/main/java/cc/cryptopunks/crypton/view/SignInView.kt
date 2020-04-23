package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import android.widget.EditText
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.service.AccountForm.*
import cc.cryptopunks.crypton.util.Form
import cc.cryptopunks.crypton.util.TextField
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.bindings.textChanges
import cc.cryptopunks.crypton.widget.ActorLayout
import kotlinx.android.synthetic.main.sign_up.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SignView(context: Context) : ActorLayout(context) {

    init {
        View.inflate(
            context,
            R.layout.sign_up,
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
                registerButton.clicks().map { OnClick },
                formFields.textFieldChanges()
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
        registerButton.text = "Sing in"
        confirmPasswordLayout.visibility = View.GONE
    }

    fun register() = apply {
        registerButton.text = "Sing up"
        confirmPasswordLayout.visibility = View.VISIBLE
    }
}