package day22

import getInput
import runLevels
import kotlin.collections.getOrElse
import kotlin.collections.windowed
import kotlin.collections.zipWithNext

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


fun solveLevel2Brute(secretNumbers: List<Long>): Long {

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

fun solveLevel2Smart(secretNumbers: List<Long>): Long {
    val allPrices = secretNumbers.map { seed -> generateSequence(seed) { t -> nextSecretNumber(t) }.take(2000).map { (it % 10).toInt() }.toList() }
    val allChanges = allPrices.map {
        it.zipWithNext().map { (first, next) -> next - first }
    }
    val lookup = allChanges.withIndex().map { (monkeyIdx, changes) ->
        changes.withIndex().windowed(4).reversed().associate { values -> values.map { it.value } to allPrices[monkeyIdx][values.last().index + 1] }
    }.fold(mutableMapOf<List<Int>, Long>()) { acc, map ->
        for (entry in map) {
            if(acc.contains(entry.key)){
                acc[entry.key] = acc[entry.key]!! + entry.value.toLong()
            } else {
                acc[entry.key] = entry.value.toLong()
            }
        }
        acc
    }
    val best = lookup.maxBy { it.value }

    println("Best combo: ${best.key}")

    return best.value
}
const val USE_SAMPLE = false

fun main() {
    val text = getInput(22, USE_SAMPLE)

    val secretNumbers = text.split("\n").filter { it.isNotBlank() }.map { it.toLong() }

    runLevels(
        22,
        { solveLevel1(secretNumbers) },
        { solveLevel2Smart(secretNumbers) },
        times = 1
    )
}