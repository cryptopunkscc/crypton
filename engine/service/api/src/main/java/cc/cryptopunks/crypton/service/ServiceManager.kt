package cc.cryptopunks.crypton.service

import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceManager @Inject constructor() {

    private var list = listOf<WeakReference<out ServiceBinding>>()
        get() = field.filterNotEmpty().also { list = it }

    fun createBinding() = ServiceBinding().also {
        list = list + WeakReference(it)
    }

    fun remove(binding: ServiceBinding) {
        list = list.filter { it.get() != binding }
    }

    fun top() = list.lastOrNull()?.get()

    fun stack(): List<ServiceBinding> = list.mapNotNull { it.get() }

    fun clear() {
        list.mapNotNull { it.get() }.forEach { it.clear() }
        list = emptyList()
    }

    private fun <T> List<WeakReference<out T>>.filterNotEmpty() = filterNot {
        it.get() == null
    }

    interface Core {
        val serviceManager: ServiceManager
    }
}