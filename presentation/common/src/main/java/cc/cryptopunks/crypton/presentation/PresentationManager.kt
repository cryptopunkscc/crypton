package cc.cryptopunks.crypton.presentation

import cc.cryptopunks.crypton.presenter.Presenter
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PresentationManager @Inject constructor() {

    private var list = listOf<WeakReference<out Presentation<*, *>>>()
        get() = field.filterNotEmpty().also { list = it }

    fun <A, P : Presenter<A>>create() = synchronized(this) {
        Presentation<A, P>().also {
            list = list + WeakReference(it)
        }
    }

    fun remove(presentation: Presentation<*, *>) = synchronized(this) {
        list = list.filter { it.get() != presentation }
    }

    fun top(): Presentation.Snapshot? = synchronized(this) {
        list.last().get()?.Snapshot()
    }

    fun stack() = synchronized(this) {
        list.mapNotNull { it.get()?.Snapshot() }
    }

    private fun <T> List<WeakReference<out T>>.filterNotEmpty() = filterNot {
        it.get() == null
    }

    interface Component {
        val presentationManager: PresentationManager
    }
}