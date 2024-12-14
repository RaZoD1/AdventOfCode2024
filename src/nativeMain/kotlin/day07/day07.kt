package day07

import getInput
import kotlin.time.measureTimedValue


fun isCalculationFixable(result: Long, numbers: List<Long>): Boolean {

    val possibleResults = listOf(numbers[0] + numbers[1], numbers[0] * numbers[1])
    for (possibleResult in possibleResults) {
        if (possibleResult > result) continue

        val newList = numbers.toMutableList().also {
            it.removeFirst()
            it[0] = possibleResult
        }.toList()

        if (numbers.size == 2) {
            if (possibleResult == result) return true
        } else {
            if (isCalculationFixable(result, newList)) return true
        }
    }
    return false
}

fun isCalculationFixableWithConcat(result: Long, numbers: List<Long>): Boolean {

    val possibleResults =
        listOf(numbers[0] + numbers[1], numbers[0] * numbers[1], "${numbers[0]}${numbers[1]}".toLong())
    for (possibleResult in possibleResults) {
        if (possibleResult > result) continue

        val newList = numbers.toMutableList().also {
            it.removeFirst()
            it[0] = possibleResult
        }.toList()

        if (numbers.size == 2) {
            if (possibleResult == result) return true
        } else {
            if (isCalculationFixableWithConcat(result, newList)) return true
        }
    }
    return false
}

fun solveLevel1(calculations: Map<Long, List<Long>>) {
    val result = measureTimedValue {
        calculations.filter { (result, numbers) ->
            isCalculationFixable(
                result,
                numbers
            )
        }.keys.sum()
    }.let {
        println("Took ${it.duration.inWholeMilliseconds}ms")
        it.value
    }

    println("Fixable calculations sum: $result")
}

fun solveLevel2(calculations: Map<Long, List<Long>>) {
    val result = measureTimedValue {
        calculations.filter { (result, numbers) ->
            isCalculationFixableWithConcat(
                result,
                numbers
            )
        }.keys.also { println("valid results: $it") }.sum()
    }.let {
        println("Took ${it.duration.inWholeMilliseconds}ms")
        it.value
    }

    println("Fixable calculations sum: $result")
}

fun main() {
    val text = getInput(7)

    val calculations = text.split("\n").filter { it.isNotEmpty() }.associate { line ->
        Pair(
            line.takeWhile { c -> c != ':' }.toLong(),
            line.split(":").last()
                .split(" ")
                .filter { it.isNotEmpty() }
                .map { it.toLong() })
    }

    solveLevel1(calculations)

}