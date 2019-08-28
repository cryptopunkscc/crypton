package cc.cryptopunks.crypton.core.util

import android.os.Handler
import cc.cryptopunks.crypton.common.HandleError
import io.reactivex.processors.PublishProcessor
import org.reactivestreams.Subscriber
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainErrorHandler @Inject constructor(
    private val logError: LogError,
    private val handler: Handler
) : (Throwable) -> Unit,
    HandleError,
    HandleError.Publisher {

    private val processor = PublishProcessor.create<Throwable>()

    override fun subscribe(subscriber: Subscriber<in Throwable>?) {
        processor.subscribe(subscriber)
    }

    override fun invoke(throwable: Throwable) {
        logError(throwable)
        handler.post {
            processor.onNext(throwable)
        }
    }
}

class LogError @Inject constructor() : HandleError, (Throwable) -> Unit by { throwable ->
    Timber.e(throwable)
}