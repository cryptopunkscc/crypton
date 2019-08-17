package cc.cryptopunks.crypton.core.module

import cc.cryptopunks.crypton.common.Schedulers
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Scope


@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelScope

@Module
class ViewModelModule {

    @Provides
    @ViewModelScope
    fun schedulers() = Schedulers(
        main = AndroidSchedulers.mainThread(),
        io = io.reactivex.schedulers.Schedulers.io()
    )
}