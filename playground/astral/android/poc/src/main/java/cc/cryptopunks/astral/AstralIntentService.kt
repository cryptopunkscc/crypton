package cc.cryptopunks.astral

import android.content.Intent
import android.content.Context
import android.util.Log
import androidx.core.app.JobIntentService
import astral.Astral

class AstralIntentService : JobIntentService() {
    private val tag = javaClass.simpleName

    override fun onHandleWork(intent: Intent) {
        Thread.sleep(3000)
        Log.d(tag, Thread.currentThread().name)
        Log.d(tag, Thread.currentThread().threadGroup.name)
        Log.d(tag, "Starting astrald")
        Astral.astralDir()
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, AstralIntentService::class.java)
            enqueueWork(context, AstralIntentService::class.java, 1, intent)
        }
    }
}
