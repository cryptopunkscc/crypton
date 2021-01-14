package cc.cryptopunks.crypton.util.logger

import cc.cryptopunks.crypton.util.Log

class LogOutputCache(
    private val capacity: Int,
    private val cache: MutableList<Any> = mutableListOf()
) : List<Any> by cache,
    Log.Output {

    override fun invoke(event: Any) {
        if (cache.size > capacity)
            cache.drop(cache.size - capacity)
        cache.add(event)
    }

    companion object {
        val Default = LogOutputCache(2048)
    }
}
