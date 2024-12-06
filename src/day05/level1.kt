package day05

import getResourceAsText


fun checkUpdateOrder(allRules: List<Pair<Int, Int>>, pages: List<Int>): Boolean {
    val pageSet = pages.toSet()
    val requiredPagesForPages = allRules
        .filter { rule -> pageSet.contains(rule.first) && pageSet.contains(rule.second) }
        .groupBy ({ it.second }, { it.first})

    val pagesHandled = mutableSetOf<Int>()
    for(page in pages){
        val pagesRequiredBefore = requiredPagesForPages.getOrDefault(page, emptyList())
        if(!pagesHandled.containsAll(pagesRequiredBefore)) {
            return false
        }
        pagesHandled += page
    }
    return true
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
            update[update.size/2]
        else 0
    }

    println("Result: $result")
}