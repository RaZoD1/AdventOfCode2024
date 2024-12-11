package day11

import getResourceAsText
import kotlin.time.measureTime

val STEPS = 75


val cache = mutableMapOf<Pair<Int, Long>, Long>()
fun calculateStonesAfterBlinksFor(stone: Long, steps: Int): Long {
    if(steps == 0) return 1
    if(cache.contains(Pair(steps, stone))) return cache[Pair(steps, stone)]!!

    if(stone == 0L){
        return calculateStonesAfterBlinksFor(1L, steps - 1).also { if (!cache.containsKey(Pair(steps - 1, 1L))) cache[Pair(steps - 1, 1L)] = it }
    }else if(stone.toString().length % 2 == 0){
        stone.toString().also {
            val stone1 = it.substring(0..<it.length / 2).toLong()
            val stone2 = it.substring((it.length / 2)..<it.length).toLong()

            val count1 = calculateStonesAfterBlinksFor(stone1, steps - 1).also { if (!cache.containsKey(Pair(steps - 1, stone1))) cache[Pair(steps - 1, stone1)] = it }
            val count2 = calculateStonesAfterBlinksFor(stone2, steps - 1).also { if (!cache.containsKey(Pair(steps - 1, stone2))) cache[Pair(steps - 1, stone2)] = it }

            return count1 + count2

        }
    } else {
        return calculateStonesAfterBlinksFor(stone * 2024L, steps - 1).also { if (!cache.containsKey(Pair(steps - 1, stone * 2024L))) cache[Pair(steps - 1, stone * 2024L)] = it }
    }
}

fun solveLevel2(){
    val text = getResourceAsText("/day11/input") ?: error("Input not found")


    val stones = text.replace("\n", "").split(" ").map { it.toLong() }

    val amountOfStones = stones.sumOf { calculateStonesAfterBlinksFor(it, STEPS) }

    println("Amount of Stones: $amountOfStones")
    println("Cache size: ${cache.size}")
}

fun main() {
    measureTime { solveLevel2() }.also { println("Time taken (part2): ${it.inWholeMilliseconds}ms") }
}