package crypton.ops.util

data class Changelog(
    val version: List<Int>,
    val code: Int,
    val commit: String,
    val map: Map<Change.Type, List<Change>>,
    val latest: Boolean
) {
    val formattedVersion = version.formatVersion()
}

fun Project.buildChangeLog(changes: List<Change>, latest: Boolean) = Changelog(
    version = versionName,
    code = versionCode,
    map = changes.groupBy(Change::conventionalType).toMap(),
    latest = latest,
    commit = Git.headSha()
)

fun Changelog.incrementVersion(project: Project) = when {
    commit == project.versionHash -> VersionPart.None
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
                .updateSha(Git.headSha())
        )
        else -> copy(
            version = versionName(project.projectPath)
                .increment(part.index),
            code = versionCode(project.projectPath)
                .increment(0).first(),
            commit = versionHash(project.projectPath)
                .updateSha(Git.headSha())
        )
    }
}

private val patches = Change.Type.values().filter { it.version == VersionPart.Patch }
