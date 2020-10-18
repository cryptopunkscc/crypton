package cc.cryptopunks.crypton.navigate

import android.content.Context
import android.os.Bundle
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.address
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private const val GLOBAL_SHARED_PREFS = "global_shard_prefs"


fun bundle(build: (Bundle) -> Unit) = Bundle().apply(build)

val Context.sharedPrefs get() = getSharedPreferences(GLOBAL_SHARED_PREFS, Context.MODE_PRIVATE)


var Bundle.account by BundleAddress

var Bundle.chat by BundleAddress

var Context.currentAccount by ContextAddress


private object BundleAddress : ReadWriteProperty<Bundle, Address?> {

    override fun getValue(thisRef: Bundle, property: KProperty<*>) =
        thisRef.getString(property.name)?.let { address(it) }

    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Address?) {
        thisRef.putString(property.name, value?.id)
    }
}

private object ContextAddress : ReadWriteProperty<Context, Address> {

    override fun getValue(thisRef: Context, property: KProperty<*>) =
        thisRef.sharedPrefs.getString(property.name, null)?.let { address(it) } ?: Address.Empty

    override fun setValue(thisRef: Context, property: KProperty<*>, value: Address) {
        thisRef.sharedPrefs.edit().putString(property.name, value.id).apply()
    }
}
