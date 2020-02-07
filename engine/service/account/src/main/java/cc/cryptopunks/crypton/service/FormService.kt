package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.util.Form
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.util.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FormService(
    override val coroutineContext: CoroutineContext
) : Form.Service {

    private val state = State(Form())

    val form get() = state.get()

    override fun Service.Connector.connect(): Job = launch {
        input.filterIsInstance<Form.Service.Input>().collect { arg ->
            when (arg) {
                is Form.Set -> state { arg.form }
                is Form.SetField -> state { setField(arg.field) }
                is Form.Submit -> Form.Error(null).out()
            }
        }
    }
}