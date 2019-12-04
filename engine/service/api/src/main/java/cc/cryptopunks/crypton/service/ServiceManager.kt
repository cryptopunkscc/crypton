package cc.cryptopunks.crypton.service

import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceManager @Inject constructor() {

    private var list = listOf<WeakReference<out ServiceBinding>>()
        get() = field.filterNotEmpty().also { list = it }

    fun create() = ServiceBinding().also {
        list = list + WeakReference(it)
    }

    fun remove(binding: ServiceBinding) {
        list = list.filter { it.get() != binding }
    }

    fun top(): ServiceBinding.Snapshot? = list.lastOrNull()?.get()?.snapshot()

    fun stack() = list.mapNotNull { it.get()?.snapshot() }

    private fun <T> List<WeakReference<out T>>.filterNotEmpty() = filterNot {
        it.get() == null
    }

    interface Core {
        val serviceManager: ServiceManager
    }
}