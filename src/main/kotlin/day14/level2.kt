package day14

import getResourceAsText
import plotVec2s
import kotlin.time.measureTime


fun mayContainTree(robots: List<Robot>): Boolean {
    return groupByQuads(robots).any { it.value.size > robots.size /2 }
}


fun printRobots(robots: List<Robot>) {
    plotVec2s(robots.map { it.p })
}


fun solveLevel2(text: String) {
    val robots = text.split("\n").map { Robot.parse(it) }

    var secs = 0
    while(true){
        secs++
        for (robot in robots) {
            robot.takeStep()
        }
        if(mayContainTree(robots)) {
            printRobots(robots)
            println(secs)
            readln()
        }
    }

}

fun main() {
    val text = getResourceAsText("/day14/${if(sample) "sample" else "input"}") ?: error("Input not found")
    measureTime { solveLevel2(text) }.also {
        println("Time: $it")
    }
}