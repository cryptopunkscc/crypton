package cc.cryptopunks.crypton.core.module

import cc.cryptopunks.crypton.core.util.BaseFragment
import dagger.Module
import dagger.Provides

@Module
class BaseFragmentModule(
    @get:Provides
    val fragment: BaseFragment
) {
    @Provides
    fun BaseFragment.fragmentManager() = childFragmentManager

    @Provides
    fun BaseFragment.viewDisposable() = viewDisposable

    @Provides
    fun BaseFragment.modelDisposable() = modelDisposable
}