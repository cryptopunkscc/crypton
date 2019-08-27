package cc.cryptopunks.crypton.core.module

import androidx.fragment.app.Fragment
import cc.cryptopunks.crypton.core.util.BaseFragment
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Scope


@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentScope

@Module
class FragmentModule(
    private val fragment: Fragment
) {

    @Provides
    @FragmentScope
    fun fragmentManager() = fragment.childFragmentManager

    @Provides
    @FragmentScope
    fun disposable() = CompositeDisposable()
}


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