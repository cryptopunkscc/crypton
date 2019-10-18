package cc.cryptopunks.crypton.notification

import android.app.Application
import android.widget.Toast
import cc.cryptopunks.crypton.entity.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class ShowMessageNotification @Inject constructor(
    private val application: Application
) : Message.Sys.ShowNotification, (Message) -> Unit by { message ->
    GlobalScope.launch(Dispatchers.Main) {
        // TODO replace with system notification
        Toast.makeText(application, message.toString(), Toast.LENGTH_SHORT).show()
    }
}