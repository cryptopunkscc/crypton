package crypton.ops.util

object Git {
    internal fun headSha(depth: Int = 0): String =
        "git rev-parse HEAD~$depth".execShell()

    internal fun commitSha(from: String): String =
        "git log --pretty=%H $from^..$from".execShell()

//    internal fun latestTag(): String =
//        "git describe --abbrev=0 --tags".execShell()

    internal fun latestTag(): String =
        "git tag --list".execShell().lineSequence().first { it.startsWith("v") }

    internal fun messages(from: String = latestTag(), to: String = "HEAD"): String =
        "git log $from..$to --pretty=%B".execShell()
}
