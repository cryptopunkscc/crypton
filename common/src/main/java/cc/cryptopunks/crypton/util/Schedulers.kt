package cc.cryptopunks.crypton.util

import io.reactivex.*
import io.reactivex.schedulers.Schedulers as RxSchedulers

data class Schedulers(
    val main: Scheduler,
    val io: Scheduler
) {
    companion object {
        private val currentThreadScheduler = RxSchedulers.from { it.run() }

        val currentThread = Schedulers(
            main = currentThreadScheduler,
            io = currentThreadScheduler
        )
    }
}

fun <T> Flowable<T>.runOn(schedulers: Schedulers) = this
    .subscribeOn(schedulers.io)
    .observeOn(schedulers.main)!!

fun <T> Observable<T>.runOn(schedulers: Schedulers) = this
    .subscribeOn(schedulers.io)
    .observeOn(schedulers.main)!!

fun <T> Single<T>.runOn(schedulers: Schedulers) = this
    .subscribeOn(schedulers.io)
    .observeOn(schedulers.main)!!

fun <T> Maybe<T>.runOn(schedulers: Schedulers) = this
    .subscribeOn(schedulers.io)
    .observeOn(schedulers.main)!!

fun Completable.runOn(schedulers: Schedulers) = this
    .subscribeOn(schedulers.io)
    .observeOn(schedulers.main)!!