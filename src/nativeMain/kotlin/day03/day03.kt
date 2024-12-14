package day03

import getInput

val multiplicationRegex = Regex("""mul\([0-9]{1,3},[0-9]{1,3}\)""", RegexOption.MULTILINE)
fun solveLevel1(text: String) {
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


val instructionsRegex = Regex("""(mul\([0-9]{1,3},[0-9]{1,3}\))|(do\(\))|(don't\(\))""", RegexOption.MULTILINE)
fun solveLevel2(text: String) {
    val instructions = instructionsRegex.findAll(text)

    var mulActive = true

    val sum = instructions.map { it.value }.sumOf { instruction ->
        instruction.also { println(it) }
        if (instruction == "do()") {
            mulActive = true
            0
        } else if (instruction == "don't()") {
            mulActive = false
            0
        } else if (instruction.startsWith("mul(")) {
            if (!mulActive) 0 else
                instruction.let {
                    it.drop(4).dropLast(1).split(",").map { it.toInt() }
                }.let {
                    it.fold(1) { left, right -> left * right }
                }.toLong()
        } else {
            error("Instruction unknown: $instruction")
        }

    }
    println("Sum of multiplications: $sum")
}


fun main() {
    val text = getInput(3)
    solveLevel1(text)
    solveLevel2(text)
}