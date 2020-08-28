package crypton.ops.util

import java.io.File
import java.lang.StringBuilder
import java.util.Comparator

private const val RELEASE_NOTES_MD = "release_notes.md"
private const val NEW_RELEASE_NOTES_MD = "$RELEASE_NOTES_MD.new"
private const val LATEST_NOTES_MD = "latest_notes.md"

fun Project.updateLatestNotes(): File =
    file(LATEST_NOTES_MD).apply {
        if (version.snapshotHash != Git.headSha(1)) {
            if (!exists()) createNewFile()
            generateReleaseNotes(latest = true).apply {
                println(project)
                project.writeVersion()
                writeReleaseNotes()
            }
        }
    }

fun Project.updateReleaseNotes(): File {
    val releaseNotes = file(RELEASE_NOTES_MD).apply {
        if (!exists()) createNewFile()
    }
    val notesTag = releaseNotes.reader().buffered().readLine()?.run {
        removePrefix("## ").split("build").first().trim()
    }
    val latestTag = Git.latestTag().split("-").first()
    if (latestTag != notesTag)

        file(NEW_RELEASE_NOTES_MD).apply {
            if (!exists()) createNewFile()

            releaseNotes.reader().buffered().lineSequence().forEach { line ->
                appendText("$line\n")
            }

            copyTo(releaseNotes, true)
            delete()
        }

    return releaseNotes
}


fun Project.generateReleaseNotes(
    from: String = Git.latestTag(),
    to: String = "HEAD",
    latest: Boolean = false
): Changelog =
    buildChangeLog(
        changes = Git
            .messages(from, to)
            .parseChanges()
            .toList(),
        latest = latest
    ).run {
        if (!latest) this
        else incrementVersion()
    }

private fun Changelog.writeReleaseNotes() = when (latest) {
    true -> project.file(LATEST_NOTES_MD)
    false -> project.file(RELEASE_NOTES_MD)
}.apply {
    writeText(formatReleaseNotes())
}


private fun Changelog.formatReleaseNotes(): String =
    StringBuilder().apply {
        appendln(title())
        map.toSortedMap(changeComparator).map { (type, changes) ->
            appendln(type.header())
            changes.forEach { change ->
                appendln(change.format())
            }
        }
    }.toString()

private val changeComparator = Comparator<Change.Type> { l, r -> l.ordinal.compareTo(r.ordinal) }

private fun Changelog.title() = "v$formattedVersion build ${version.build}".let { version ->
    if (latest) "Snapshot $version"
    else version
}.markdownH(2)

private fun Change.Type.header() = when (this) {
    Change.Type.BREAKING_CHANGE -> "Breaking changes"
    Change.Type.feat -> "New features"
    Change.Type.fix -> "Bug fixes"
    Change.Type.refactor -> "Refactor"
    Change.Type.ci -> "CI"
    Change.Type.doc -> "Documentation"
    Change.Type.chore -> "Chore"
    else -> "Other"
}.markdownH(3)

private fun Change.format() = "* $description"
