package cc.cryptopunks.crypton.util.logger

import cc.cryptopunks.crypton.util.Log

class LogOutputCache(
    private val capacity: Int,
    private val cache: MutableList<Log.Event> = mutableListOf()
) : List<Log.Event> by cache,
    Log.Output {

    override fun invoke(event: Log.Event) {
        if (cache.size > capacity)
            cache.drop(cache.size - capacity)
        cache.add(event)
    }

    companion object {
        val Default = LogOutputCache(2048)
    }
}
