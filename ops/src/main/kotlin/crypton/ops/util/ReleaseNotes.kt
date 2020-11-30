package crypton.ops.util

import java.io.File
import java.lang.StringBuilder
import java.util.Comparator

private const val RELEASE_NOTES_MD = "release_notes.md"
private const val NEW_RELEASE_NOTES_MD = "$RELEASE_NOTES_MD.new"
private const val LATEST_NOTES_MD = "latest_notes.md"

fun Project.updateSnapshotNotes(): File =
    file(LATEST_NOTES_MD).apply {
        if (!exists()) createNewFile()
        generateReleaseNotes(latest = true).apply {
            project.writeVersion()
            writeReleaseNotes()
        }
    }

fun Project.updateVersionNotes(): File {
    val versionNotes = file(RELEASE_NOTES_MD).apply {
        if (!exists()) createNewFile()
    }

    file(NEW_RELEASE_NOTES_MD).apply {
        if (!exists()) createNewFile()

        generateReleaseNotes().apply {
            project.writeVersion()
            writeReleaseNotes()
        }

        versionNotes.forEachLine { line -> appendText("$line\n") }

        copyTo(versionNotes, true)

        delete()
    }

    return versionNotes
}


fun Project.generateReleaseNotes(
    latest: Boolean = false,
    from: String = when {
        latest -> version.hash
        else -> version.hash
    }.takeIf { it.isNotEmpty() } ?: Git.latestTag(),
    to: String = "HEAD"
): Changelog =
    buildChangeLog(
        changes = Git
            .messages(from, to)
            .parseChanges()
            .toList(),
        latest = latest
    ).run {
        if (!latest) incrementHash()
        else incrementVersion()
    }

private fun Changelog.writeReleaseNotes() = when (latest) {
    true -> project.file(LATEST_NOTES_MD)
    false -> project.file(NEW_RELEASE_NOTES_MD)
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
