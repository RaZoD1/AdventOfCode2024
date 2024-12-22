package day19


import getInput
import runLevels


val cache = mutableMapOf<String, Long>()
fun possibleTowelArrangements(towels: Set<String>, design: String): Long {

    if (cache.containsKey(design)) return cache[design]!!

    val possibleTowels = towels.filter { towel -> design.startsWith(towel) }
    var arrangements = 0L

    for (towel in possibleTowels) {
        val designLeft = design.drop(towel.length)
        if (designLeft.isEmpty()) {
            arrangements += 1
        } else {
            arrangements += possibleTowelArrangements(towels, designLeft)
        }
    }
    cache[design] = arrangements
    return arrangements
}

fun solveLevel1(towels: Set<String>, designs: List<String>): Long {
    return designs.count { possibleTowelArrangements(towels, it) > 0 }.toLong()
}

fun solveLevel2(towels: Set<String>, designs: List<String>): Long {
    return designs.sumOf { possibleTowelArrangements(towels, it) }.toLong()
}


fun main() {
    val text = getInput(19, false)
    val lines = text.split('\n')

    val towels = lines[0].split(", ").toSet()

    val designs = lines.drop(2).filter { it.isNotBlank() }

    runLevels(
        19,
        { solveLevel1(towels, designs) },
        { solveLevel2(towels, designs) },
        times = 1
    )
}
