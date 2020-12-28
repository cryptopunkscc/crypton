package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.CoroutineContextTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.system.measureNanoTime

private const val times: Int = 1000

private object TestObj1 : CoroutineContextTag
private object TestObj2 : CoroutineContextTag
private object TestObj3 : CoroutineContextTag
private object TestObj4 : CoroutineContextTag
private object TestObj5 : CoroutineContextTag
private object TestObj6 : CoroutineContextTag
private object TestObj7 : CoroutineContextTag
private object TestObj8 : CoroutineContextTag
private object TestObj9 : CoroutineContextTag
private object TestObj10 : CoroutineContextTag
private object TestObj11 : CoroutineContextTag
private object TestObj12 : CoroutineContextTag
private object TestObj13 : CoroutineContextTag
private object TestObj14 : CoroutineContextTag
private object TestObj15 : CoroutineContextTag
private object TestObj16 : CoroutineContextTag
private object TestObj17 : CoroutineContextTag
private object TestObj18 : CoroutineContextTag
private object TestObj19 : CoroutineContextTag
private object TestObj20 : CoroutineContextTag
private object TestObj21 : CoroutineContextTag
private object TestObj22 : CoroutineContextTag
private object TestObj23 : CoroutineContextTag
private object TestObj24 : CoroutineContextTag
private object TestObj25 : CoroutineContextTag
private object TestObj26 : CoroutineContextTag
private object TestObj27 : CoroutineContextTag
private object TestObj28 : CoroutineContextTag
private object TestObj29 : CoroutineContextTag

class DependencyTest {


    private val CoroutineScope.test1 by dep<TestObj1>()
    private val CoroutineScope.test15 by dep<TestObj15>()
    private val CoroutineScope.test29 by dep<TestObj29>()

    private val CoroutineScope.test2_1 get() = coroutineContext[TestObj1]!!
    private val CoroutineScope.test2_15 get() = coroutineContext[TestObj15]!!
    private val CoroutineScope.test2_29 get() = coroutineContext[TestObj29]!!

    private val list = listOf(
        TestObj1,
        TestObj2,
        TestObj3,
        TestObj4,
        TestObj5,
        TestObj6,
        TestObj7,
        TestObj8,
        TestObj9,
        TestObj10,
        TestObj11,
        TestObj12,
        TestObj13,
        TestObj14,
        TestObj15,
        TestObj16,
        TestObj17,
        TestObj18,
        TestObj19,
        TestObj20,
        TestObj21,
        TestObj22,
        TestObj23,
        TestObj24,
        TestObj25,
        TestObj26,
        TestObj27,
        TestObj28,
        TestObj29,
    )
    val classes = list.map { it.javaClass }
    private val randomList = mutableListOf<CoroutineContextTag>().apply {
        repeat(times) {
            add(list.random())
        }
    }
    val randomClasses = randomList.map { it.javaClass }
    private val map: Map<CoroutineContextTag, CoroutineContextTag> = list.associateBy { it }
    private val depList: List<Dependency<out CoroutineContextTag>> = listOf(
        TestObj1.asDep(),
        TestObj2.asDep(),
        TestObj3.asDep(),
        TestObj4.asDep(),
        TestObj5.asDep(),
        TestObj6.asDep(),
        TestObj7.asDep(),
        TestObj8.asDep(),
        TestObj9.asDep(),
        TestObj10.asDep(),
        TestObj11.asDep(),
        TestObj12.asDep(),
        TestObj13.asDep(),
        TestObj14.asDep(),
        TestObj15.asDep(),
        TestObj16.asDep(),
        TestObj17.asDep(),
        TestObj18.asDep(),
        TestObj19.asDep(),
        TestObj20.asDep(),
        TestObj21.asDep(),
        TestObj22.asDep(),
        TestObj23.asDep(),
        TestObj24.asDep(),
        TestObj25.asDep(),
        TestObj26.asDep(),
        TestObj27.asDep(),
        TestObj28.asDep(),
        TestObj29.asDep(),
    )
    private val randomDepList = mutableListOf<Dependency<out CoroutineContextTag>>().apply {
        repeat(times) {
            add(depList.random())
        }
    }
    private val context1 = depList.fold(EmptyCoroutineContext as CoroutineContext) { acc, dependency -> acc + dependency }
    private val context2 = list.fold(EmptyCoroutineContext as CoroutineContext) { acc, dependency -> acc + dependency }

    @Test
    fun test1() {
        runBlocking(context1) {
            assertEquals(
                TestObj1,
                test1
            )
        }
    }

    @Test
    fun test2() {
        var tmp: Any = Unit
        println("accessor:")
        repeat(times) { measureNanoTime { tmp = times } }
        repeatSumPrint(times) { measureNanoTime { tmp = times } }
        println()

        println("map:")
        println("1:")
        repeat(times) { measureNanoTime { tmp = map[TestObj1]!! } }
        repeatSumPrint(times) { measureNanoTime { tmp = map[TestObj1]!! } }
        println("15:")
        repeat(times) { measureNanoTime { tmp = map[TestObj15]!! } }
        repeatSumPrint(times) { measureNanoTime { tmp = map[TestObj15]!! } }
        println("29:")
        repeat(times) { measureNanoTime { tmp = map[TestObj29]!! } }
        repeatSumPrint(times) { measureNanoTime { tmp = map[TestObj29]!! } }
        println("random:")
        randomList.sumOf {measureNanoTime {  tmp = map[it]!! } }
        println(randomList.sumOf {measureNanoTime {  tmp = map[it]!! } })
        println()

        runBlocking(context1) {
            println("property:")
            println("1:")
            repeat(times) { measureNanoTime { tmp = test1 } }
            repeatSumPrint(times) { measureNanoTime { tmp = test1 } }
            println("15:")
            repeat(times) { measureNanoTime { tmp = test15 } }
            repeatSumPrint(times) { measureNanoTime { tmp = test15 } }
            println("29:")
            repeat(times) { measureNanoTime { tmp = test29 } }
            repeatSumPrint(times) { measureNanoTime { tmp = test29 } }
            println("random:")
            randomDepList.sumOf { measureNanoTime { tmp = coroutineContext[it.key]!! } }
            println(randomDepList.sumOf { measureNanoTime { tmp = coroutineContext[it.key]!! } })
            println()
        }

        runBlocking(context2) {
            println("getter:")
            println("1:")
            repeat(times) { measureNanoTime { tmp = test2_1 } }
            repeatSumPrint(times) { measureNanoTime { tmp = test2_1 } }
            println("15:")
            repeat(times) { measureNanoTime { tmp = test2_15 } }
            repeatSumPrint(times) { measureNanoTime { tmp = test2_15 } }
            println("29:")
            repeat(times) { measureNanoTime { tmp = test2_29 } }
            repeatSumPrint(times) { measureNanoTime { tmp = test2_29 } }
            println("random:")
            randomList.sumOf { measureNanoTime { tmp = coroutineContext[it]!! } }
            println(randomList.sumOf { measureNanoTime { tmp = coroutineContext[it]!! } })
            println()
        }
    }
}

private fun repeatSumPrint(times: Int, action: () -> Long) {
    println((0..times).sumOf { action() })
}
