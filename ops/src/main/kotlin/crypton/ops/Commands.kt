package crypton.ops

import cc.cryptopunks.crypton.translator.Commands
import cc.cryptopunks.crypton.translator.command
import cc.cryptopunks.crypton.translator.param
import crypton.ops.util.BUILD_VERSION
import crypton.ops.util.Git
import crypton.ops.util.MAJOR
import crypton.ops.util.MINOR
import crypton.ops.util.PATCH
import crypton.ops.util.increment
import crypton.ops.util.project
import crypton.ops.util.updateLatestNotes
import crypton.ops.util.updateReleaseNotes

val commands: Commands = mapOf("main" to mapOf(
    "increment" to mapOf(
        "major" to command(param()) { (path) -> increment(path, MAJOR) },
        "minor" to command(param()) { (path) -> increment(path, MINOR) },
        "patch" to command(param()) { (path) -> increment(path, PATCH) },
        "version" to command(param()) { (path) -> increment(path, BUILD_VERSION) }
    ),
    "generate" to mapOf(
        "notes" to mapOf(
            "latest" to command { project(route).updateLatestNotes() },
            "release" to command { project(route).updateReleaseNotes() }
        )
    ),
    "print" to mapOf(
        "changes" to command { Git.messages() }
    )
))
