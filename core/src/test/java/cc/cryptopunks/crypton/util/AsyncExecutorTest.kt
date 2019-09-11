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

        val test1 = async.wrap(Task)(Unit).test()
        val test2 = async.wrap(Task)(Unit).test()

        sleep(DELAY/2)

        verify(exactly = 2) { runningTasks.add(taskContext) }
        assertEquals(1, runningTasks.size)
        assertEquals(1, runningTasks.values.first().size)

        sleep(DELAY/2)

        assertEquals(0, runningTasks.size)

        test1.assertComplete().assertNoErrors()
        test2.assertComplete().assertNoErrors()
    }

    object Task : (Any) -> Completable by {
        Completable.fromAction {
            sleep(DELAY)
        }
    }

    companion object {
        private const val DELAY = 300L
    }
}