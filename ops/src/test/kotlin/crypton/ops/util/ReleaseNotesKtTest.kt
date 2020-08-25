package crypton.ops.util

import org.junit.Test
import java.io.File

class ReleaseNotesKtTest {

    private val project = project(File("").absoluteFile.toPath().parent)

    @Test
    fun `should generate release notes`() {
        println(project.generateReleaseNotes())
    }

    @Test
    fun `should update latest notes`() {
        project.updateLatestNotes()
    }

    @Test
    fun `should update release notes`() {
        project.updateReleaseNotes()
    }
}
