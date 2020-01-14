package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import android.widget.EditText
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.bindings.textChanges
import cc.cryptopunks.crypton.service.AccountForm.*
import cc.cryptopunks.crypton.util.Field
import cc.cryptopunks.crypton.util.Form
import cc.cryptopunks.crypton.util.TextField
import cc.cryptopunks.crypton.widget.ServiceLayout
import kotlinx.android.synthetic.main.sign_up.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SignView(context: Context) : ServiceLayout(context) {

    init {
        View.inflate(
            context,
            R.layout.sign_up,
            this
        )
    }

    private val formFields: Map<Field.Id, EditText>
        get() = mapOf(
            ServiceName to serviceName,
            UserName to userName,
            Password to password,
            ConfirmPassword to confirmPassword
        )

    override fun Service.Connector.connect(): Job = launch {
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
            .map { (view, text) -> view?.setText(text as Field.Text) }
    }


    private fun Map<Field.Id, EditText>.textFieldChanges() = map { (id, editText) ->
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