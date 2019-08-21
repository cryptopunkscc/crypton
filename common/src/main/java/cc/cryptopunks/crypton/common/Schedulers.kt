package cc.cryptopunks.crypton.common

import io.reactivex.Scheduler

data class Schedulers(
    val main: Scheduler,
    val io: Scheduler
)