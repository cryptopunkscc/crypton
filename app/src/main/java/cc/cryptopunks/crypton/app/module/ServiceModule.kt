package cc.cryptopunks.crypton.app.module

import android.app.Service
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ServiceScope

@Module
class ServiceModule(
    @get:Provides
    val service: Service
)