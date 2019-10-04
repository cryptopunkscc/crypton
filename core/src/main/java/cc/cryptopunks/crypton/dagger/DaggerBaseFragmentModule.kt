package cc.cryptopunks.crypton.dagger

import cc.cryptopunks.crypton.util.BaseFragment
import dagger.Module
import dagger.Provides

@Module
class DaggerBaseFragmentModule(
    @get:Provides
    val fragment: BaseFragment
) {
    @Provides
    fun BaseFragment.fragmentManager() = childFragmentManager
}