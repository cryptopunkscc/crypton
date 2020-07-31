package cc.cryptopunks.crypton.util.logger

import cc.cryptopunks.crypton.util.Log

class BufferLog(
    private val capacity: Int,
    private val cache: MutableList<Log.Event>
) : List<Log.Event> by cache,
    Log.Output {

    override fun invoke(event: Log.Event) {
        if (cache.size > capacity)
            cache.drop(cache.size - capacity)
        cache.add(event)
    }
}
