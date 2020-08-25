package crypton.ops.util

object Git {
    internal fun headSha(): String =
        "git rev-parse HEAD".execShell()

    internal fun commitSha(from: String): String =
        "git log --pretty=%H $from^..$from".execShell()

    internal fun latestTag(): String =
        "git describe --abbrev=0 --tags".execShell()

    internal fun messages(from: String, to: String = "HEAD"): String =
        "git log $from..$to --pretty=%B".execShell()
}
