package cc.cryptopunks.crypton.dagger

import android.app.Service
import android.content.Context
import dagger.Module
import dagger.Provides

@Module(includes = [
    DaggerContextModule::class
])
class DaggerServiceModule {
    @Provides
    fun service(context: Context) = context as Service
}