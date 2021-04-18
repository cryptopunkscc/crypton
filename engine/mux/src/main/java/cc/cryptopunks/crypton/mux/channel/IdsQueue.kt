package cc.cryptopunks.crypton.mux.channel

class IdsQueue(
    val list: MutableList<Short> = mutableListOf(),
    var max: Short = 0,
)

internal fun IdsQueue.next(): Short =
    list.removeFirstOrNull() ?: max++

internal operator fun IdsQueue.plusAssign(id: Short) {
    list += id
}
