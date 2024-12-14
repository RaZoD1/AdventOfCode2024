package day13

import LVec2
import getInput
import org.jetbrains.kotlinx.multik.api.linalg.solve
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.operations.map
import org.jetbrains.kotlinx.multik.ndarray.operations.toList
import runLevels
import kotlin.math.roundToLong

data class ClawMachine(val btnA: LVec2, val btnB: LVec2, val prize: LVec2) {
    companion object {
        const val BTN_A_COST = 3
        const val BTN_B_COST = 1

        fun parse(text: String): ClawMachine {
            val (btnALine, btnBLine, prizeLine) = text.split("\n")
            val btnAX = btnALine.substringAfter("X+").substringBefore(",").toLong()
            val btnAY = btnALine.substringAfter("Y+").toLong()
            val btnBX = btnBLine.substringAfter("X+").substringBefore(",").toLong()
            val btnBY = btnBLine.substringAfter("Y+").toLong()
            val prizeX = prizeLine.substringAfter("X=").substringBefore(",").toLong()
            val prizeY = prizeLine.substringAfter("Y=").toLong()

            return ClawMachine(LVec2(btnAX, btnAY), LVec2(btnBX, btnBY), LVec2(prizeX, prizeY))
        }
    }


    fun findMinimumAmountOfTokens(): Long {
        val coefficients = mk.ndarray(
            mk[
                mk[btnA.x, btnB.x],
                mk[btnA.y, btnB.y]
            ]
        ).map { it.toDouble() }

        val solutions = mk.ndarray(
            mk[prize.x, prize.y]
        ).map { it.toDouble() }

        val (a, b) = mk.linalg.solve(coefficients, solutions).map { it.roundToLong() }.toList().also {
            println(it)
        }

        if (btnA * a + btnB * b != prize) return 0

        return BTN_A_COST * a + BTN_B_COST * b
    }
}

fun solveLevel1(clawMachines: List<ClawMachine>): Long {
    val minimumTotalAmountOfTokens = clawMachines.sumOf { it.findMinimumAmountOfTokens() }

    println("Min. total amount of Tokens (part1): $minimumTotalAmountOfTokens")
    return minimumTotalAmountOfTokens
}

const val CONVERSION_ERROR = 10000000000000
fun solveLevel2(clawMachines: List<ClawMachine>): Long {
    val minimumTotalAmountOfTokens = clawMachines
        .map { it.copy(prize = it.prize + LVec2(CONVERSION_ERROR, CONVERSION_ERROR)) }
        .sumOf { it.findMinimumAmountOfTokens() }

    println("Min. total amount of Tokens (part2): $minimumTotalAmountOfTokens")
    return minimumTotalAmountOfTokens
}

fun main() {
    val text = getInput(13)
    val clawMachines = text.split("\n\n").filter { it.isNotEmpty() }.map {
        ClawMachine.parse(it)
    }
    runLevels(13, { solveLevel1(clawMachines) }, { solveLevel2(clawMachines) })
}

