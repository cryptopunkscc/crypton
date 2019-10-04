package cc.cryptopunks.crypton.dagger

import android.app.Service
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ServiceScope

@Module
class DaggerServiceModule(
    @get:Provides
    @get:ServiceScope
    val service: Service
)