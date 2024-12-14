package day12

import Vec2
import getResourceAsText
import kotlin.time.measureTime
import kotlin.time.measureTimedValue


val CORNER_MATRIX = mapOf(
    Vec2(0,0) to listOf(Vec2.LEFT, Vec2.UP_LEFT, Vec2.UP),
    Vec2(1, 0) to listOf(Vec2.UP, Vec2.UP_RIGHT, Vec2.RIGHT),
    Vec2(1, 1) to listOf(Vec2.RIGHT, Vec2.DOWN_RIGHT, Vec2.DOWN),
    Vec2(0, 1) to listOf(Vec2.DOWN, Vec2.DOWN_LEFT, Vec2.LEFT)
)

fun areaSides(area: Set<Vec2>): Long {
    val corners = mutableSetOf<Vec2>()
    var extra = 0
    for (pos in area) {
        if(DIRECTIONS.all { (pos + it) in area }) continue
        for ((offset, corner) in CORNER_MATRIX) {
            if(corner.count { (pos + it) !in area } % 2 == 1)
                corners.add(pos + offset)
            else if(listOf(corner.first(), corner.last()).all { (pos + it) !in area })
                if(!corners.contains(pos +offset)) corners.add(pos + offset)
                else extra++
        }
    }
    return corners.size.toLong() + extra
}

fun solveLevel2(text: String){
    val grid = text.split("\n").filter { it.isNotEmpty() }.map { it.toList() }

    val areas: List<Set<Vec2>> = measureTimedValue { findAreas(grid) }.let {
        println("Time to find areas: ${it.duration}")
        it.value
    }

    val totalPrice = areas.sumOf { area -> areaSides(area)* area.size }

    println("Total price (part 2): $totalPrice")
}

fun main() {
    val text = getResourceAsText("/day12/input")?.replace("\r", "") ?: error("Input not found")
    measureTime {  solveLevel2(text) }.also {
        println("Time: $it")
    }

}