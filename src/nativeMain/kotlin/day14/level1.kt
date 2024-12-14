package day14

import Vec2
import getResourceAsText
import kotlin.time.measureTime

const val sample = false
val SIZE = if(sample) Vec2(col = 11, row = 7) else Vec2(101, 103)


data class Robot(var p: Vec2, val v: Vec2){
    val initP = p

    companion object{
        fun parse(line: String): Robot {
            val (pL, vL) = line.split(" ")
            val p = pL.drop(2).split(",").map { it.toInt() }.let { Vec2(it.first(), it.last()) }
            val v = vL.drop(2).split(",").map { it.toInt() }.let { Vec2(it.first(), it.last()) }
            return Robot(p, v)
        }
    }

    fun takeStep(){
        this.p += v
        this.p = Vec2((this.p.col + SIZE.col * 4) % SIZE.col, (this.p.row + SIZE.row * 4) % SIZE.row)
    }


}

fun groupByQuads(robots: List<Robot>): Map<Int, List<Robot>>{
    return robots.groupBy {
        if(it.p.col < (SIZE.col / 2)){
            if(it.p.row < (SIZE.row / 2)){
                1
            } else if (it.p.row > (SIZE.row / 2)){
                3
            }else {
                0
            }
        }else if(it.p.col > (SIZE.col / 2)){
            if(it.p.row < (SIZE.row / 2)){
                2
            } else if (it.p.row > (SIZE.row / 2)){
                4
            }else {
                0
            }
        } else {
            0
        }
    }
}

fun solveLevel1(text: String) {
    val robots = text.split("\n").map { Robot.parse(it) }
    robots.forEach { robot -> repeat(100) {robot.takeStep()} }
    val quads = groupByQuads(robots)
    println(robots.joinToString("\n"))
    val safetyScore = (1..4).fold(1) { mult, quad ->
        mult * quads.getOrDefault(quad, emptyList()).also { println("Quad $quad contains ${it.size} robots") }.size
    }

    println("Safety Score: $safetyScore")
}

fun main() {
    val text = getResourceAsText("/day14/${if(sample) "sample" else "input"}") ?: error("Input not found")
    measureTime { solveLevel1(text) }.also {
        println("Time: $it")
    }
}