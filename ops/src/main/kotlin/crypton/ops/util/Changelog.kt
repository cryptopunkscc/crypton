package crypton.ops.util

data class Changelog(
    val versionName: List<Int>,
    val code: Int,
    val commit: String,
    val map: Map<Change.Type, List<Change>>,
    val latest: Boolean,
    val version: Version = Version()
) {
    val formattedVersion = versionName.formatVersion()
}

fun Project.buildChangeLog(changes: List<Change>, latest: Boolean) = Changelog(
    versionName = versionName,
    code = versionCode,
    map = changes.groupBy(Change::conventionalType).toMap(),
    latest = latest,
    commit = Git.headSha(1),
    version = version
)

fun Changelog.incrementVersion(project: Project) = when {
    map.containsKey(Change.Type.BREAKING_CHANGE) -> VersionPart.Minor
    map.keys.intersect(patches).isNotEmpty() -> VersionPart.Patch
    map.keys.isNotEmpty() -> VersionPart.Code
    else -> VersionPart.None
}.let { part ->
    when(part) {
        VersionPart.None -> this
        VersionPart.Code -> copy(
            code = versionCode(project.projectPath)
                .increment(0).first(),
            commit = versionHash(project.projectPath)
                .updateSha(Git.headSha()),
            version = version.copy(
                build = version.build + 1,
                hash = if (latest) Git.headSha() else version.hash,
                snapshotHash = if (!latest) Git.headSha() else version.hash
            ).also(project::write)
        )
        else -> copy(
            versionName = versionName(project.projectPath)
                .increment(part.index),
            code = versionCode(project.projectPath)
                .increment(0).first(),
            commit = versionHash(project.projectPath)
                .updateSha(Git.headSha()),
            version = version.copy(
                build = version.build + 1,
                name = version.name.increment(part.index),
                hash = if (latest) Git.headSha() else version.hash,
                snapshotHash = if (!latest) Git.headSha() else version.hash
            ).also(project::write)
        )
    }
}

private val patches = Change.Type.values().filter { it.version == VersionPart.Patch }
