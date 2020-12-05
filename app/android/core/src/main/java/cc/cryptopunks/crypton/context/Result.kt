package cc.cryptopunks.crypton.context

import android.content.Intent

data class ActivityResult(
    val requestCode: Int,
    val resultCode: Int,
    val intent: Intent
)

data class PermissionsResult(
    val requestCode: Int,
    val permissions: List<String>,
    val grantResults: List<Int>
)
