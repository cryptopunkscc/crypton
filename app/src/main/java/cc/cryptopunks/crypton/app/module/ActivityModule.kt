package cc.cryptopunks.crypton.app.module

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import cc.cryptopunks.crypton.common.ActivityScope
import cc.cryptopunks.crypton.common.OptionItemSelectedBroadcast
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(
    private val activity: Activity,
    private val optionItemSelectedBroadcast: OptionItemSelectedBroadcast
) {

    @Provides
    @ActivityScope
    fun appCompatActivity() = activity as AppCompatActivity

    @Provides
    @ActivityScope
    fun fragmentActivity() = activity as FragmentActivity

    @Provides
    @ActivityScope
    fun activity(): Activity = activity

    @Provides
    @ActivityScope
    fun layoutInflater() = LayoutInflater.from(activity)!!

    @Provides
    @ActivityScope
    fun fragmentManager(activity: FragmentActivity) = activity.supportFragmentManager!!

    @Provides
    @ActivityScope
    fun broadcast() = optionItemSelectedBroadcast
}

