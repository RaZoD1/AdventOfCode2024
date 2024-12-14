package day07

import getResourceAsText
import kotlin.time.measureTimedValue


fun isCalculationFixableWithConcat(result: Long, numbers: List<Long>): Boolean {

    val possibleResults = listOf(numbers[0] + numbers[1], numbers[0] * numbers[1], "${numbers[0]}${numbers[1]}".toLong())
    for(possibleResult in possibleResults){
        if(possibleResult > result) continue

        val newList = numbers.toMutableList().also {
            it.removeFirst()
            it[0] = possibleResult
        }.toList()

        if(numbers.size == 2){
            if(possibleResult == result) return true
        } else {
            if(isCalculationFixableWithConcat(result, newList)) return true
        }
    }
    return false
}

fun main() {
    val text = getResourceAsText("/day07/input") ?: error("Input not found")

    val calculations = text.split("\n").filter { it.isNotEmpty() }.associate { line ->
        Pair(
            line.takeWhile { c -> c != ':' }.toLong(),
            line.split(":").last()
                .split(" ")
                .filter { it.isNotEmpty() }
                .map { it.toLong() })
    }

    val result = measureTimedValue { calculations.filter { (result, numbers) -> isCalculationFixableWithConcat(result, numbers) }.keys.also { println("valid results: $it") }.sum()}.let {
        println("Took ${it.duration.inWholeMilliseconds}ms")
        it.value
    }

    println("Fixable calculations sum: $result")
}