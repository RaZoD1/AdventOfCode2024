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

    list1.sort()
    list2.sort()

    val differenceSum = list1.zip(list2).sumOf {
        abs(it.first - it.second)
    }

    println("Sum: $differenceSum")
}