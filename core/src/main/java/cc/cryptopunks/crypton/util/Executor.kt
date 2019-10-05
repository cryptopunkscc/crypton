package cc.cryptopunks.crypton.util

import java.util.concurrent.Executor

class MainExecutor(executor: Executor) : Executor by executor
class IOExecutor(executor: Executor) : Executor by executor

interface ExecutorsComponent {
    val mainExecutor: MainExecutor
    val ioExecutor: IOExecutor
}

class ExecutorsModule(
    override val mainExecutor: MainExecutor,
    override val ioExecutor: IOExecutor
) : ExecutorsComponent