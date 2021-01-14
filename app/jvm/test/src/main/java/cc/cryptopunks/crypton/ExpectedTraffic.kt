package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.logv2.Log
import cc.cryptopunks.crypton.logv2.d
import cc.cryptopunks.crypton.serial.list

class ExpectedTraffic(
    private val log: Log<Unit, Any>
) {
    private val expected = mutableListOf<(Any) -> Any?>()
    val traffic = mutableListOf<Any>()

    fun <T> next(vararg context: Address, check: T.() -> Unit) {
        val exception = Exception()
        expected.add {
            try {
                val t =
                    if (it !is Scoped) it else it.list().minus(context.map(
                        Address::id)).apply {
                        require(size == 1) {
                            "Invalid $this context: ${context.toList()} arg: $it"
                        }
                    }.first()
                check(t as T)
            } catch (e: Throwable) {
                TrafficError(
                    message = e.message,
                    cause = exception
                )
            }
        }
    }

    fun <T> lazy(check: T.() -> Unit) {
        expected.add {
            try {
                check(it as T)
                true
            } catch (e: Throwable) {
                log.d { "WARNING: lazy check skipping $it" }
                false
            }
        }
    }

    fun check(value: Any) = expected.run {
        traffic.add(value)
        firstOrNull()?.let { check ->
            when (val result = check(value)) {
                null -> Unit
                is Unit -> removeAt(0)
                is Throwable -> throw result.also {
                    it.printStackTrace()
                }
                is Boolean -> if (result) removeAt(0) else Unit
                else -> throw Error("unknown result $result")
            }
        }
    }
}

class TrafficError(
    message: String?,
    cause: Throwable
) : Exception(message, cause)
