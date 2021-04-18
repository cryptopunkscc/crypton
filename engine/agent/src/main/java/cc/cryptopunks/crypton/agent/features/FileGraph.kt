package cc.cryptopunks.crypton.agent.features

import cc.cryptopunks.crypton.Async
import cc.cryptopunks.crypton.agent.decodeStoryHeader
import cc.cryptopunks.crypton.agent.encodeDatagram
import cc.cryptopunks.crypton.agent.fileStreamChunks
import cc.cryptopunks.crypton.create.cryptonContext
import cc.cryptopunks.crypton.create.handler
import cc.cryptopunks.crypton.delegate.dep
import cc.cryptopunks.crypton.fsv2.Graph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach


val CoroutineScope.fileGraph: Graph.ReadWrite by dep()

object GraphRequest {
    data class Target(val id: String) : Async
    data class Source(val id: String) : Async
    data class Type(val value: String) : Async
    object AllStories : Async
}


fun fileGraph() = cryptonContext(

    handler { _, (id): FileAdded ->
        runCatching {
            val bytes = fileStore[id].fold(ByteArray(0)) { acc, bytes -> acc + bytes }
            bytes.decodeStoryHeader()
        }.onSuccess { story ->
            fileGraph.setType(id, story.type)
            story.rel.forEach { target -> fileGraph.setRelation(target, id) }
        }
    },

    handler { _, (id): FileRemoved -> fileGraph.remove(id) },

    handler { out, (id): GraphRequest.Target ->
        val store = fileStore
        fileGraph.target(id)
            .asFlow()
            .flatMapMerge { store.fileStreamChunks(it) }
            .collect(out)
    },

    handler { out, (id): GraphRequest.Source ->
        fileStore.fileStreamChunks(fileGraph.source(id)).collect(out)
    },

    handler { out, (id): GraphRequest.Target ->
        fileStore.fileStreamChunks(fileGraph.target(id)).collect(out)
    },

    handler { out, (type): GraphRequest.Type ->
        fileStore.fileStreamChunks(fileGraph.type(type)).collect(out)
    },

    handler { out, _: GraphRequest.AllStories ->
        fileStore.fileStreamChunks(fileGraph.stories())
            .onEach { print(it.bytes.toString(Charsets.UTF_8)) } // TODO print
            .map { encodeDatagram(it) }
            .collect(out)
    }
)
