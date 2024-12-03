package day03

import getResourceAsText

val multiplicationRegex = Regex("""mul\([0-9]{1,3},[0-9]{1,3}\)""", RegexOption.MULTILINE)

fun main(){
    val text = getResourceAsText("/day03/input") ?: error("Input not found")

    val multiplications = multiplicationRegex.findAll(text)

    val sum = multiplications.sumOf { multMatch ->
        multMatch.value.also { println(it) }
        multMatch.value.let {
            it.drop(4).dropLast(1).split(",").map { it.toInt() }
        }.let {
            it.fold(1) { left, right -> left * right }
        }.toLong()
    }

    println("Sum of multiplications: $sum")
}