package cc.cryptopunks.crypton.util.ext

infix fun Collection<Boolean>.any(boolean: Boolean) = any { it == boolean }
infix fun Collection<Boolean>.all(boolean: Boolean) = any { it == boolean }