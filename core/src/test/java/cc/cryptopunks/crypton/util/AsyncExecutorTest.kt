package cc.cryptopunks.crypton.util

import io.mockk.spyk
import io.mockk.verify
import io.reactivex.Completable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.lang.Thread.sleep

class AsyncExecutorTest {

    private val handleError = handleError {  }
    private lateinit var runningTasks: RunningTasks
    private lateinit var async: AsyncExecutor

    @Before
    fun setUp() {
        runningTasks = spyk(RunningTasks())
        async = AsyncExecutor(
            handleError = handleError,
            runningTasks = runningTasks
        )
    }

    @Test
    operator fun invoke() {
        val taskContext = TaskContext(task = Task, arg = Unit)

        async(task = Task)(Unit)
        async(task = Task)(Unit)

        verify(exactly = 2) { runningTasks.add(taskContext) }
        assertEquals(1, runningTasks.size)
        assertEquals(1, runningTasks.values.first().size)
        sleep(DELAY)
        assertEquals(0, runningTasks.size)
    }

    object Task : (Any) -> Completable by {
        Completable.fromAction {
            sleep(DELAY)
        }
    }

    companion object {
        private const val DELAY = 100L
    }
}