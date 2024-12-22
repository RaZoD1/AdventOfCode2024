package day17

import getInput
import runLevels

class Computer(val program: List<Int>, var regA: Long = 0, var regB: Long = 0, var regC: Long = 0) {
    val initA = regA
    val initB = regB
    val initC = regC

    var ip = 0
    val output = mutableListOf<Long>()


    fun reset() {
        regA = initA
        regB = initB
        regC = initC
        ip = 0
        output.clear()
    }

    fun runUntilHalt() {
        do {
            val notHalted = next()
        } while (notHalted)
    }

    fun next(): Boolean {
        if (program.size <= ip + 1) return false
        val operator = program[ip]
        val operand = program[ip + 1]
        val instruction = when (operator) {
            0 -> ::adv
            1 -> ::bxl
            2 -> ::bst
            3 -> ::jnz
            4 -> ::bxc
            5 -> ::out
            6 -> ::bdv
            7 -> ::cdv
            else -> error("Unknown instruction $operator")
        }
        instruction.invoke(operand).also { ret ->
            if (ret is Boolean && ret == true) {
                // jump: skip incrementing instruction pointer
            } else {
                ip += 2
            }
        }

        return true
    }

    fun adv(combo: Int) {
        regA = regA / (1L shl resolveCombo(combo).toInt())
    }

    fun bdv(combo: Int) {
        regB = regA / (1L shl resolveCombo(combo).toInt())
    }

    fun cdv(combo: Int) {
        regC = regA / (1L shl resolveCombo(combo).toInt())
    }

    fun bxl(literal: Int) {
        regB = literal.toLong() xor regB
    }

    fun bxc(ignored: Int) {
        regB = regB xor regC
    }

    fun bst(combo: Int) {
        regB = resolveCombo(combo) % 8
    }

    fun jnz(literal: Int): Boolean {
        if (regA == 0L) {
            return false
        } else {
            ip = literal
            return true
        }
    }

    fun out(combo: Int) {
        output += resolveCombo(combo) % 8
    }


    fun resolveCombo(combo: Int): Long = when (combo) {
        in 1..3 -> combo
        4 -> regA
        5 -> regB
        6 -> regC
        else -> error("Unknown combo operand $combo")
    }.toLong()

}

fun solveLevel1(computer: Computer): Long {
    computer.runUntilHalt()
    val output = computer.output.joinToString(",")
    println(output)
    return -1
}


fun findAReg(aReg: Long, program: List<Int>): Long {

    if (program.isEmpty()) return aReg
    val goal = program.last().toLong()
    for (b in 0L..7L) {
        val a = (aReg shl 3) xor b
        //println("Trying ${b.toString(2)} for goal $goal at index ${program.size - 1} with a=${a.toString(2)};$a")
        if ((b xor 0b100 xor (a shr (b xor 0b1).toInt())) % 8 == goal) {
            //println("Found b ${b.toString(2)} for goal $goal")
            val res = findAReg(a, program.dropLast(1))
            if (res == -1L) continue
            else return res
        }
    }

    return -1
}

fun solveLevel2(computer: Computer): Long {

    return findAReg(0, computer.program)

}

fun main() {
    val text = getInput(17, alternateFileName = "input")
    val lines = text.split('\n')
    val regA = lines[0].substringAfter(": ").toLong()
    val regB = lines[1].substringAfter(": ").toLong()
    val regC = lines[2].substringAfter(": ").toLong()
    val program = lines[4].substringAfter(": ").split(",").map { it.toInt() }


    runLevels(
        17,
        { solveLevel1(Computer(program, regA, regB, regC)) },
        { solveLevel2(Computer(program, regA, regB, regC)) },
    )
}
