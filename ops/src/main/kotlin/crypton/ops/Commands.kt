package crypton.ops

import cc.cryptopunks.crypton.cliv2.Cli
import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.commands
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.cliv2.param
import crypton.ops.util.BUILD_VERSION
import crypton.ops.util.Git
import crypton.ops.util.MAJOR
import crypton.ops.util.MINOR
import crypton.ops.util.PATCH
import crypton.ops.util.increment
import crypton.ops.util.project
import crypton.ops.util.updateSnapshotNotes
import crypton.ops.util.updateVersionNotes

val commands: Cli.Commands = commands(
    "increment" to mapOf(
        "major" to command(param()) { (path) -> increment(path, MAJOR) },
        "minor" to command(param()) { (path) -> increment(path, MINOR) },
        "patch" to command(param()) { (path) -> increment(path, PATCH) },
        "version" to command(param()) { (path) -> increment(path, BUILD_VERSION) }
    ),
    "generate" to mapOf(
        "notes" to mapOf(
            "snapshot" to command(config("path")) { (path) -> project(path).updateSnapshotNotes() },
            "version" to command(config("path")) { (path) -> project(path).updateVersionNotes() }
        )
    ),
    "print" to mapOf(
        "latest-tag" to command { Git.latestTag() },
        "changes" to command { Git.messages() }
    )
)
