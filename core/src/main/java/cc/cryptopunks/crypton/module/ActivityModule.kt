package cc.cryptopunks.crypton.module

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope

@Module
class ActivityModule(
    @get:Provides
    @get:ActivityScope
    val activity: Activity
) {

    @Provides
    @ActivityScope
    fun fragmentActivity() = activity as FragmentActivity

    @Provides
    @ActivityScope
    fun appCompatActivity() = activity as AppCompatActivity

    @Provides
    @ActivityScope
    fun layoutInflater() = LayoutInflater.from(activity)!!

    @Provides
    @ActivityScope
    fun fragmentManager(activity: FragmentActivity) = activity.supportFragmentManager!!
}

