package day05

import getInput
import runLevels


fun checkUpdateOrder(allRules: List<Pair<Int, Int>>, pages: List<Int>): Boolean {
    val pageSet = pages.toSet()
    val requiredPagesForPages = allRules
        .filter { rule -> pageSet.contains(rule.first) && pageSet.contains(rule.second) }
        .groupBy({ it.second }, { it.first })

    val pagesHandled = mutableSetOf<Int>()
    for (page in pages) {
        val pagesRequiredBefore = requiredPagesForPages.getOrElse(page) { emptyList() }
        if (!pagesHandled.containsAll(pagesRequiredBefore)) {
            return false
        }
        pagesHandled += page
    }
    return true
}

fun solveLevel1(rules: List<Pair<Int, Int>>, updates: List<List<Int>>): Long {
    val result = updates.sumOf { update ->
        if (checkUpdateOrder(rules, update))
            update[update.size / 2]
        else 0
    }

    println("Result: $result")
    return result.toLong()
}

fun fixUpdateOrder(allRules: List<Pair<Int, Int>>, pages: List<Int>): List<Int> {
    val pageSet = pages.toSet()
    val requiredPagesForPages = allRules
        .filter { rule -> pageSet.contains(rule.first) && pageSet.contains(rule.second) }
        .groupBy({ it.second }, { it.first })

    val orderedPages = pages.sortedBy { page -> requiredPagesForPages.getOrElse(page) { emptyList() }.size }

    return orderedPages
}

fun solveLevel2(rules: List<Pair<Int, Int>>, updates: List<List<Int>>): Long {
    val result = updates.sumOf { update ->
        if (checkUpdateOrder(rules, update))
            0
        else {
            fixUpdateOrder(rules, update).let { it[it.size / 2] }
        }
    }

    println("Result: $result")
    return result.toLong()
}

fun main() {
    val text = getInput(5)

    val rules = text.split("\n\n").first().split("\n").filter { it.isNotEmpty() }
        .map { line ->
            line.split("|")
                .map { it.toInt() }.let { Pair(it.first(), it.last()) }
        }

    val updates = text.split("\n\n").last().split("\n").filter { it.isNotEmpty() }
        .map { line -> line.split(",").map { it.toInt() } }

    runLevels(5, { solveLevel1(rules, updates) }, { solveLevel2(rules, updates) })
}