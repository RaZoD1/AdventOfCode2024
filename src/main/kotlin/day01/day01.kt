package day01

import getInput
import runLevels
import kotlin.math.abs

fun solveLevel1(text: String): Long {

    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()


    text.split("\n")
        .filter { it.isNotEmpty() }
        .forEach {
            it.split("   ").also {
                list1.add(it.first().toInt())
                list2.add(it.last().toInt())
            }
        }

    list1.sort()
    list2.sort()

    val differenceSum = list1.zip(list2).sumOf {
        abs(it.first - it.second)
    }

    println("Sum: $differenceSum")
    return differenceSum.toLong()
}

fun solveLevel2(text: String): Long {
    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()


    text.split("\n")
        .filter { it.isNotEmpty() }
        .forEach {
            it.split("   ").also {
                list1.add(it.first().toInt())
                list2.add(it.last().toInt())
            }
        }

    val rightCounts = list2.groupBy { it }.mapValues { it.value.size }

    val similarityScore = list1.sumOf {
        it * rightCounts.getOrElse(it) { 0 }
    }

    println("Similarity Score: $similarityScore")
    return similarityScore.toLong()
}

fun main() {
    val text = getInput(1)
    runLevels(1, { solveLevel1(text) }, { solveLevel2(text) })
}