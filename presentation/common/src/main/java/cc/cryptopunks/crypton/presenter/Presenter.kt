package cc.cryptopunks.crypton.presenter

import cc.cryptopunks.crypton.actor.Actor

interface Presenter<in A : Actor> {
    suspend operator fun invoke(): Any = Unit
    suspend operator fun A.invoke(): Any = Unit

    interface Component<P : Presenter<*>> {
        val presenter: P
        fun inject(presenter: P)
    }
}