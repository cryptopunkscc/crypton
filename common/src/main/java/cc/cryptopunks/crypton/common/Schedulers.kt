package cc.cryptopunks.crypton.common

import io.reactivex.*

data class Schedulers(
    val main: Scheduler,
    val io: Scheduler
)

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