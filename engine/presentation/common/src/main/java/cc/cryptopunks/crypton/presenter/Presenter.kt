package cc.cryptopunks.crypton.presenter

interface Presenter<in A> {
    suspend operator fun invoke(): Any = Unit
    suspend operator fun A.invoke(): Any = Unit
}