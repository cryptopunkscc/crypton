package cc.cryptopunks.crypton.sys

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import cc.cryptopunks.crypton.context.Device

class DeviceSys(
    private val context: Context
) : Device.Sys {

    @SuppressLint("HardwareIds")
    override fun deviceId(): String = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )
}
