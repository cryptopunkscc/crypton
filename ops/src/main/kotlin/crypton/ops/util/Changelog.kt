package crypton.ops.util

data class Changelog(
    val map: Map<Change.Type, List<Change>>,
    val latest: Boolean,
    val version: Version = Version(),
    val project: Project = Project()
) {
    val formattedVersion = version.name.formatVersion()
}

fun Project.buildChangeLog(changes: List<Change>, latest: Boolean) = Changelog(
    map = changes.groupBy(Change::conventionalType).toMap(),
    latest = latest,
    version = version,
    project = this
)

fun Changelog.incrementVersion() = when {
    map.containsKey(Change.Type.BREAKING_CHANGE) -> VersionPart.Minor
    map.keys.intersect(patches).isNotEmpty() -> VersionPart.Patch
    map.keys.isNotEmpty() -> VersionPart.Code
    else -> VersionPart.None
}.let { part ->
    when (part) {
        VersionPart.None -> this
        VersionPart.Code -> copy(
            version = version.copy(
                build = version.build + 1,
                hash = if (!latest) Git.headSha() else version.hash,
                snapshotHash = if (latest) Git.headSha() else version.hash
            )
        )
        else -> copy(
            version = version.copy(
                build = version.build + 1,
                name = version.name.increment(part.index),
                hash = if (!latest) Git.headSha() else version.hash,
                snapshotHash = if (latest) Git.headSha() else version.hash
            )
        )
    }
}.run { copy(project = project.copy(version = version)) }

private val patches = Change.Type.values().filter { it.version == VersionPart.Patch }
