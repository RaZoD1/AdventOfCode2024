package day22

import getInput
import runLevels

const val MOD = 16777216L

fun nextSecretNumber(n: Long): Long {
    var res = n
    res = ((res shl 6) xor res) % MOD
    res = ((res ushr 5) xor res) % MOD
    res = ((res shl 11) xor res) % MOD

    return res
}

fun solveLevel1(secretNumbers: List<Long>): Long {
    return secretNumbers.sumOf { secret ->
        var secret = secret
        repeat(2000) { secret = nextSecretNumber(secret) }
        secret
    }
}

fun solveLevel2(secretNumbers: List<Long>): Long {
    return -1
}
const val USE_SAMPLE = false

fun main() {
    val text = getInput(22, USE_SAMPLE)

    val secretNumbers = text.split("\n").filter { it.isNotBlank() }.map { it.toLong() }

    runLevels(
        22,
        { solveLevel1(secretNumbers) },
        { solveLevel2(secretNumbers) },
        times = 1
    )
}