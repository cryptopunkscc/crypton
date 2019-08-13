package cc.cryptopunks.crypton.core.util

import io.reactivex.Scheduler

data class Schedulers(
    val main: Scheduler,
    val io: Scheduler
)