package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.util.Field
import cc.cryptopunks.crypton.util.Form
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.util.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FormService(
    override val coroutineContext: CoroutineContext
) : Service {

    private val state = State(Form())

    val form get() = state.get()

    override fun Service.Connector.connect(): Job = launch {
        input.collect { arg ->
            when (arg) {
                is Form -> state { arg }
                is Field -> state { setField(arg) }
                is Form.Submit -> Form.Error(null).out()
            }
        }
    }
}