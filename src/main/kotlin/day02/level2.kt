package day02

import getResourceAsText

fun isReportValidWithDampener(report: List<Int>):Boolean {

    if(isReportValid(report)) return true

    for(i in report.indices){
        if(isReportValid(report.filterIndexed { index, _ -> index != i }))
            return true
    }
    return false

}

fun main(){
    val text = getResourceAsText("/day02/input") ?: error("Input not found")

    val reports = text.split("\n").filter { it.isNotEmpty() }.map {
        it.split(" ").map { it.toInt() }
    }

    val validReports = reports.filter { isReportValidWithDampener(it) }

    println("Amount of valid reports (with dampener): ${validReports.size}")
}