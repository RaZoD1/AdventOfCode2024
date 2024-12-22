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


fun buyBananas(a: Int, b: Int, c: Int, d: Int, allPrices: List<List<Int>>, allChanges: List<List<Int>>): Long {
    return allChanges.withIndex().sumOf { (monkeyIdx, changes) ->
        val buy = changes.withIndex().windowed(4).firstOrNull {
            it[0].value == a && it[1].value == b && it[2].value == c && it[3].value == d
        }
        if(buy == null) 0L
        else allPrices[monkeyIdx].getOrElse(buy[3].index + 1) {0}.toLong()
    }
}

fun solveLevel2(secretNumbers: List<Long>): Long {



    val mostBananas = (-9..9).toList().parallelStream().map { i->
        val prices = secretNumbers.map { seed -> generateSequence(seed) { t -> nextSecretNumber(t) }.take(2000).map { (it % 10).toInt() }.toList() }
        val changes = prices.map {
            it.zipWithNext().map { (first, next) -> next - first }
        }
        println("Stream for i = $i started")
        var mostBananas = 0L
        var winningCombo = emptyList<Int>()

        var lower = (-9 - i).coerceAtLeast(-9)
        var upper = (9 - i).coerceAtMost(9)
        for(j in lower..upper){
            lower = (-9 - j).coerceAtLeast(-9)
            upper = (9 - j).coerceAtMost(9)
            for(l in lower..upper){
                lower = (-9 - l).coerceAtLeast(-9)
                upper = (9 - l).coerceAtMost(9)
                for(m in lower..upper){
                    buyBananas(i, j, l, m, prices, changes)
                        .also { if(it > mostBananas) {
                            mostBananas = it
                            winningCombo = listOf(i, j, l, m)
                        } }
                }
            }
            println("Finnished i=$i j=$j")
        }
        println("STOPPED Stream for i = $i winner $winningCombo with bananas $mostBananas")

        mostBananas to winningCombo
    }.toList().maxBy { it.first }

    println("Winning combo ${mostBananas.second}")

    return mostBananas.first
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