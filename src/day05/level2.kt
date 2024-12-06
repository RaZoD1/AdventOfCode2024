package day05

import getResourceAsText


fun fixUpdateOrder(allRules: List<Pair<Int, Int>>, pages: List<Int>): List<Int> {
    val pageSet = pages.toSet()
    val requiredPagesForPages = allRules
        .filter { rule -> pageSet.contains(rule.first) && pageSet.contains(rule.second) }
        .groupBy ({ it.second }, { it.first})

    val orderedPages = pages.sortedBy { page -> requiredPagesForPages.getOrDefault(page, emptyList()).size }

    return orderedPages
}


fun main(){
    val text = getResourceAsText("/day05/input") ?: error("Input not found")

    val rules = text.split("\n\n").first().split("\n").filter { it.isNotEmpty() }
        .map { line -> line.split("|")
            .map { it.toInt() }.let { Pair(it.first(), it.last()) }
        }

    val updates = text.split("\n\n").last().split("\n").filter { it.isNotEmpty() }
        .map { line -> line.split(",").map { it.toInt() } }


    val result = updates.sumOf { update ->
        if(checkUpdateOrder(rules, update))
            0
        else {
            fixUpdateOrder(rules, update).let { it[it.size/2]}
        }
    }

    println("Result: $result")
}