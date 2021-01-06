package cc.cryptopunks.crypton.log

import cc.cryptopunks.crypton.RequestLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

fun Flow<List<RequestLog.Data>>.filterLatest() = filter {
    when (it.last().data) {
        is RequestLog.Event.Finish, is Throwable -> true
        else -> false
    }
}
