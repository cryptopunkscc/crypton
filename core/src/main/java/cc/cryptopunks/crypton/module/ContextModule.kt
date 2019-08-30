package cc.cryptopunks.crypton.module

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.getSystemService
import cc.cryptopunks.crypton.util.DisposableDelegate
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ContextScope

@Module
class ContextModule(

    @get:Provides
    @get:ContextScope
    val context: Context
) {

    @Provides
    @ContextScope
    fun disposable() = (context as? DisposableDelegate)
        ?.disposable as? CompositeDisposable
        ?: CompositeDisposable()

    @Provides
    @ContextScope
    fun Context.notificationManager(): NotificationManager = getSystemService()!!
}