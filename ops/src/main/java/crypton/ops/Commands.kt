package crypton.ops

import cc.cryptopunks.crypton.translator.Commands
import cc.cryptopunks.crypton.translator.command
import cc.cryptopunks.crypton.translator.param

private val increment = "increment" to mapOf(
    "major" to command(param()) { (path) ->
        increment(
            path,
            MAJOR
        )
    },
    "minor" to command(param()) { (path) ->
        increment(
            path,
            MINOR
        )
    },
    "patch" to command(param()) { (path) ->
        increment(
            path,
            PATCH
        )
    },
    "version" to command(param()) { (path) ->
        increment(
            path,
            BUILD_VERSION
        )
    }
)

val commands: Commands = mapOf(Unit to mapOf(increment))
