package cc.cryptopunks.crypton.module

import android.app.Activity
import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class AppModule(
    @get:Provides
    val application: Application,

    @get:Provides
    val mainActivityClass: Class<out Activity>
)