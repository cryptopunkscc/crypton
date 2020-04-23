package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.util.Form
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FormService(
    override val coroutineContext: CoroutineContext
) : Form.Service {

    private val store = Store(Form())

    val form get() = store.get()

    override fun Connector.connect(): Job = launch {
        input.filterIsInstance<Form.Service.Input>().collect { arg ->
            when (arg) {
                is Form.Set -> store { arg.form }
                is Form.SetField -> store { setField(arg.field) }
                is Form.Submit -> Form.Error(null).out()
            }
        }
    }
}