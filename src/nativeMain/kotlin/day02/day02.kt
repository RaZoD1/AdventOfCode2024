package day02

import getInput
import kotlin.math.sign

fun isReportValid(report: List<Int>): Boolean {
    val toCheck = report.zipWithNext()
    val direction = toCheck.first().let { sign((it.second - it.first).toDouble()).toInt() }

    for (check in toCheck) {
        val absDifference = (check.second - check.first) * direction
        if (absDifference < 1 || absDifference > 3) {
            return false
        }
    }
    return true
}


fun isReportValidWithDampener(report: List<Int>): Boolean {

    if (isReportValid(report)) return true

    for (i in report.indices) {
        if (isReportValid(report.filterIndexed { index, _ -> index != i }))
            return true
    }
    return false

}

fun solveLevel1(reports: List<List<Int>>) {
    val validReports = reports.filter { isReportValid(it) }

    println("Amount of valid reports: ${validReports.size}")
}

fun solveLevel2(reports: List<List<Int>>) {
    val validReports = reports.filter { isReportValidWithDampener(it) }

    println("Amount of valid reports (with dampener): ${validReports.size}")
}

fun main() {
    val text = getInput(2)

    val reports = text.split("\n").filter { it.isNotEmpty() }.map {
        it.split(" ").map { it.toInt() }
    }
    solveLevel1(reports)
    solveLevel2(reports)
}