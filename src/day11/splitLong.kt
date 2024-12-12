package day11

import kotlin.math.pow
import kotlin.system.measureTimeMillis


fun splitNumberString(number: Long): Pair<Long, Long> {
    val numberStr = number.toString()
    val midIndex = numberStr.length / 2

    val firstPart = numberStr.substring(0, midIndex).toLong()
    val secondPart = numberStr.substring(midIndex).toLong()

    return Pair(firstPart, secondPart)
}

fun splitNumberArithmetic(number: Long): Pair<Long, Long> {
    val divisor = 10.0.pow(numDigits(number) / 2).toLong()

    val firstPart = number / divisor
    val secondPart = number % divisor

    return Pair(firstPart, secondPart)
}

fun main() {
    val numbers = listOf(2040L, 123456L, 98765432L, 1234567890L, 112233445566L)

    val stringMethodTime = measureTimeMillis {
        repeat(100_000) {
            for (number in numbers) {
                splitNumberString(number)
            }
        }
    }

    val arithmeticMethodTime = measureTimeMillis {
        repeat(100_000) {
            for (number in numbers) {
                splitNumberArithmetic(number)
            }
        }
    }

    println("String method time: ${stringMethodTime}ms")
    println("Arithmetic method time: ${arithmeticMethodTime}ms")
}
