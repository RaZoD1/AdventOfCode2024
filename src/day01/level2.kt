package day01

import getResourceAsText
import kotlin.math.abs

fun main(){
    val text = getResourceAsText("/day01/level1") ?: error("Input not found")

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

    val rightCounts = list2.groupBy{it}.mapValues { it.value.size }

    val similarityScore = list1.sumOf {
        it * rightCounts.getOrDefault(it, 0)
    }

    println("Similarity Score: $similarityScore")
}