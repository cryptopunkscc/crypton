package crypton.ops

import crypton.ops.util.Change
import crypton.ops.util.changeRegex
import crypton.ops.util.parseChanges
import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Test

class ChangelogKtTest {

    @Ignore("fixme")
    @Test
    fun `should parse changes`() {
        // given
        val changes = """
            a: a b c
            
            a(b): a b c
            
            a!: a b c
            
            a(b)!: a b c
            
        """.trimIndent()

        val expected = listOf(
            Change(
                type = "a",
                description = "a b c"
            ),
            Change(
                type = "a",
                description = "a b c"
            ),
            Change(
                type = "a",
                description = "a b c",
                isBreaking = true
            ),
            Change(
                type = "a",
                description = "a b c",
                isBreaking = true
            )
        )

        // when
        val actual = changes.parseChanges().toList()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun `should match regex`() {
        assertTrue("a: a b c".contains(changeRegex))
    }
}
