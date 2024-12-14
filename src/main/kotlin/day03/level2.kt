package day03

import getResourceAsText

val instructionsRegex = Regex("""(mul\([0-9]{1,3},[0-9]{1,3}\))|(do\(\))|(don't\(\))""", RegexOption.MULTILINE)

fun main(){
    val text = getResourceAsText("/day03/input") ?: error("Input not found")


    val instructions = instructionsRegex.findAll(text)

    var mulActive = true

    val sum = instructions.map { it.value }.sumOf { instruction ->
        instruction.also { println(it) }
        if(instruction == "do()") {
            mulActive = true
            0
        } else if(instruction == "don't()"){
            mulActive = false
            0
        } else if(instruction.startsWith("mul(")){
            if(!mulActive) 0 else
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