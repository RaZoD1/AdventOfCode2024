package day02

import getResourceAsText
import kotlin.math.sign

fun isReportValid(report: List<Int>):Boolean {
    val toCheck = report.zipWithNext()
    val direction = toCheck.first().let { sign((it.second - it.first).toDouble()).toInt() }

    for (check in toCheck){
        val absDifference = (check.second - check.first) * direction
        if(absDifference < 1 || absDifference > 3){
            return false
        }
    }
    return true
}

fun main(){
    val text = getResourceAsText("/day02/input") ?: error("Input not found")

    val reports = text.split("\n").filter { it.isNotEmpty() }.map {
        it.split(" ").map { it.toInt() }
    }

    val validReports = reports.filter { isReportValid(it) }

    println("Amount of valid reports: ${validReports.size}")
}