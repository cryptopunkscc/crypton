package cc.cryptopunks.crypton.util

import cc.cryptopunks.kache.core.Kache
import cc.cryptopunks.kache.core.invoke
import javax.inject.Inject
import javax.inject.Singleton

data class ActivityStack(
    val list: List<BaseActivity> = emptyList()
) {

    operator fun plus(activity: BaseActivity) = copy(list = list + activity)
    operator fun minus(activity: BaseActivity) = copy(list = list - activity)

    @Singleton
    class Cache @Inject constructor(provide: Kache.Provider) : Kache<ActivityStack> by provide()
}