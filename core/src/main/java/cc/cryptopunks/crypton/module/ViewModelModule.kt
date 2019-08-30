package cc.cryptopunks.crypton.module

import dagger.Module
import javax.inject.Scope


@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelScope

@Module
class ViewModelModule