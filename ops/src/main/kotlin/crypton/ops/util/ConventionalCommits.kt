package crypton.ops.util

import crypton.ops.util.VersionPart.Minor
import crypton.ops.util.VersionPart.None
import crypton.ops.util.VersionPart.Patch

enum class VersionPart(val index: Int) {
    Major(0),
    Minor(1),
    Patch(2),
    Code(0),
    None(-1)
}


data class Change(
    val type: String,
    val description: String,
    val isBreaking: Boolean = false
) {
    val conventionalType = conventionalType()

    @Suppress("EnumEntryName")
    enum class Type(val version: VersionPart) {
        BREAKING_CHANGE(Minor),
        feat(Patch),
        fix(Patch),
        refactor(Patch),
        doc(None),
        ci(None),
        chore(None),
        other(None)
    }
}

private fun Change.conventionalType() = when {
    isBreaking -> Change.Type.BREAKING_CHANGE
    else -> conventionalTypeMap[type] ?: Change.Type.other
}

private val conventionalTypeMap = Change.Type.values().associateBy { it.name }


fun String.parseChanges(): Sequence<Change> = this
    .lineSequence()
    .parseChange()

fun Sequence<String>.parseChange(): Sequence<Change> = this
    .filter(String::isNotEmpty)
    .mapNotNull(String::parseChange)


private fun String.parseChange() =
    if (!contains(changeRegex)) null
    else split(":", limit = 2).let { (type, description) ->
        Change(
            type = type.format(),
            description = description.trim(),
            isBreaking = type.isBreakingChange()
        )
    }

internal val changeRegex = Regex("^\\w+(\\(\\w+\\))?\\!?: .+\$")

private fun String.isBreakingChange() = endsWith("!")

private fun String.format() = split("(").first().removeSuffix("!")
