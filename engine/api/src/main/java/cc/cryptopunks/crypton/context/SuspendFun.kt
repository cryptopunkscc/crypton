package cc.cryptopunks.crypton.context

interface SuspendFun<R> {
    suspend operator fun invoke(): R
}

interface SuspendFun1<A, R> {
    suspend operator fun invoke(arg: A): R
}

interface SuspendFun2<A1, A2, R> {
    suspend operator fun invoke(arg1: A1, arg2: A2): R
}