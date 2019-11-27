package cc.cryptopunks.crypton.util

import java.util.concurrent.Executor

class MainExecutor(executor: Executor) : Executor by executor
class IOExecutor(executor: Executor) : Executor by executor

interface Executors {
    val mainExecutor: MainExecutor
    val ioExecutor: IOExecutor

    class Module(
        override val mainExecutor: MainExecutor,
        override val ioExecutor: IOExecutor
    ) : Executors
}