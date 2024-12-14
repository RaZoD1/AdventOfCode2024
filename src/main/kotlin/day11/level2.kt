package day11

import getResourceAsText
import kotlin.math.log10
import kotlin.time.measureTime

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
    return if (n == 0L) 1 else  log10(n.toDouble()).toInt() + 1
}

fun solveLevel2(text: String) {
    val stones = text.replace("\n", "").split(" ").map { it.toLong() }

    val amountOfStones = stones.sumOf { calculateStonesAfterBlinksFor(it, STEPS) }

    println("Amount of Stones: $amountOfStones")
    println("Cache size: ${cache.size}")
}

fun main() {
    val text = getResourceAsText("/day11/input") ?: error("Input not found")
    measureTime { solveLevel2(text) }.also { println("Time taken (part2): ${it}") }
}
