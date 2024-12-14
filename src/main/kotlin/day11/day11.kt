package day11

import getInput
import runLevels
import kotlin.math.log10

// import java.util.LinkedList


fun solveLevel1(text: String): Long {
    // val list = LinkedList<Long>()
    val list = mutableListOf<Long>()
    list.addAll(text.replace("\n", "").split(" ").map { it.toLong() })

    val STEPS = 25

    for (step in 0..<STEPS) {

        val iter = list.listIterator()

        while (iter.hasNext()) {
            val stone = iter.next()
            if (stone == 0L) {
                iter.set(1L)
            } else if (stone.toString().length % 2 == 0) {
                stone.toString().also {
                    it.substring(0..<it.length / 2).toLong().also { iter.set(it) }
                    it.substring((it.length / 2)..<it.length).toLong().also {
                        iter.add(it)
                        //iter.next()
                    }
                }
            } else {
                iter.set(stone * 2024L)
            }
        }

        // println("Amount of stones after ${step + 1} steps: ${list.size}")
    }

    val stoneCount = list.size

    println("Number of stones: $stoneCount")
    return stoneCount.toLong()
}

val STEPS = 75

val cache = mutableMapOf<Pair<Long, Int>, Long>()

fun calculateStonesAfterBlinksFor(stone: Long, steps: Int): Long {
    if (steps == 0) return 1

    // Retrieve from cache if available
    return cache.getOrPut(Pair(stone, steps)) {
        when {
            stone == 0L -> calculateStonesAfterBlinksFor(1L, steps - 1)
            numDigits(stone) % 2 == 0 -> {
                val (stone1, stone2) = splitNumberArithmetic(stone)

                calculateStonesAfterBlinksFor(stone1, steps - 1) +
                        calculateStonesAfterBlinksFor(stone2, steps - 1)
            }

            else -> calculateStonesAfterBlinksFor(stone * 2024L, steps - 1)
        }
    }
}

fun numDigits(n: Long): Int {
    return if (n == 0L) 1 else log10(n.toDouble()).toInt() + 1
}

fun solveLevel2(text: String): Long {
    val stones = text.replace("\n", "").split(" ").map { it.toLong() }

    val amountOfStones = stones.sumOf { calculateStonesAfterBlinksFor(it, STEPS) }

    println("Amount of Stones: $amountOfStones")
    println("Cache size: ${cache.size}")
    return amountOfStones
}

fun main() {
    val text = getInput(11)
    runLevels(11, { solveLevel1(text) }, { cache.clear(); solveLevel2(text) })
}