package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.util.BaseFragment
import dagger.Module
import dagger.Provides

@Module
class BaseFragmentModule(
    @get:Provides
    val fragment: BaseFragment
) {
    @Provides
    fun BaseFragment.fragmentManager() = childFragmentManager
}