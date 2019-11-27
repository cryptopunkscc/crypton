package cc.cryptopunks.crypton.presenter

interface Presenter<in A> {
    suspend operator fun invoke(): Any = Unit
    suspend operator fun A.invoke(): Any = Unit

    interface Component<P : Presenter<*>> {
        val presenter: P
        fun inject(presenter: P)
    }
}