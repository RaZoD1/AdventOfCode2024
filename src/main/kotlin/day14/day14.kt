package day14

import Vec2
import getInput
import plotVec2s
import runLevels
import kotlin.time.measureTime


data class Robot(var p: Vec2, val v: Vec2) {
    val initP = p

    companion object {
        fun parse(line: String): Robot {
            val (pL, vL) = line.split(" ")
            val p = pL.drop(2).split(",").map { it.toInt() }.let { Vec2(it.first(), it.last()) }
            val v = vL.drop(2).split(",").map { it.toInt() }.let { Vec2(it.first(), it.last()) }
            return Robot(p, v)
        }
    }

    fun takeStep() {
        this.p += v
        this.p = Vec2((this.p.col + SIZE.col * 4) % SIZE.col, (this.p.row + SIZE.row * 4) % SIZE.row)
    }


}

fun groupByQuads(robots: List<Robot>): Map<Int, List<Robot>> {
    return robots.groupBy {
        if (it.p.col < (SIZE.col / 2)) {
            if (it.p.row < (SIZE.row / 2)) {
                1
            } else if (it.p.row > (SIZE.row / 2)) {
                3
            } else {
                0
            }
        } else if (it.p.col > (SIZE.col / 2)) {
            if (it.p.row < (SIZE.row / 2)) {
                2
            } else if (it.p.row > (SIZE.row / 2)) {
                4
            } else {
                0
            }
        } else {
            0
        }
    }
}

fun solveLevel1(text: String): Long {
    val robots = text.split("\n").map { Robot.parse(it) }
    robots.forEach { robot -> repeat(100) { robot.takeStep() } }
    val quads = groupByQuads(robots)
    val safetyScore = (1..4).fold(1) { mult, quad ->
        mult * quads.getOrElse(quad) { emptyList() }.size
    }

    println("Safety Score: $safetyScore")
    return safetyScore.toLong()
}

fun mayContainTree(robots: List<Robot>): Boolean {
    return groupByQuads(robots).any { it.value.size > robots.size / 2 }
}


fun printRobots(robots: List<Robot>) {
    plotVec2s(robots.map { it.p })
}


fun solveLevel2(text: String): Long {
    val robots = text.split("\n").map { Robot.parse(it) }

    var secs = 0
    while (true) {
        secs++
        for (robot in robots) {
            robot.takeStep()
        }
        if (mayContainTree(robots)) {
            //printRobots(robots)
            println(secs)
            return secs.toLong()
        }
    }
}

const val USE_SAMPLE = false
val SIZE = if (USE_SAMPLE) Vec2(col = 11, row = 7) else Vec2(101, 103)
fun main() {
    val text = getInput(14, sample = USE_SAMPLE)

    runLevels(14, {solveLevel1(text)}, {solveLevel2(text)}, times = 1)

}