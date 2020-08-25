package crypton.ops.util

import org.junit.Assert.*
import org.junit.Test

class GitTest {

    @Test
    fun `should return latest tag`() {
        val actual = Git.latestTag()

        println(actual)

        assertTrue(actual.isNotEmpty())
    }

    @Test
    fun `should return messages`() {
        val tag = Git.latestTag()

        val actual = Git.messages(tag)

        assertTrue(actual.isNotEmpty())

        println(actual)
    }
}
