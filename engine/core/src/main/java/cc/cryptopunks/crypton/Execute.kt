package cc.cryptopunks.crypton

typealias Execute = suspend Request.() -> Request
typealias Execution = List<Execute>
