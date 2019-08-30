package cc.cryptopunks.crypton.util

import io.reactivex.processors.PublishProcessor
import org.reactivestreams.Processor
import org.reactivestreams.Publisher

open class Broadcast<T>(
    processor: Processor<T, T> = PublishProcessor.create()
) : Publisher<T> by processor,
        (T) -> Unit by { processor.onNext(it) }

typealias RxBroadcast<T> = Broadcast<T>