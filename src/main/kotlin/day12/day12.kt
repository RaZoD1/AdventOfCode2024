package day12

import Grid
import Vec2
import at
import getInput
import inGrid
import parseGrid
import runLevels
import kotlin.time.measureTimedValue

val DIRECTIONS = listOf(Vec2.UP, Vec2.DOWN, Vec2.RIGHT, Vec2.LEFT)


fun Grid.forEachCell(action: ((Vec2) -> Unit)) {
    for (row in this.indices) {
        for (col in this[row].indices) {
            action(Vec2(col, row))
        }
    }
}

fun findAreas(grid: Grid): List<Set<Vec2>> {
    val visited = mutableSetOf<Vec2>()
    val areas = mutableListOf<Set<Vec2>>()

    grid.forEachCell { pos ->
        if (visited.contains(pos))
            return@forEachCell
        else
            visited.add(pos)
        val plantType = grid.at(pos)


        val area = mutableSetOf<Vec2>()
        var toProcess = mutableSetOf<Vec2>(pos)
        while (toProcess.isNotEmpty()) {
            val nextToProcess = mutableSetOf<Vec2>()
            for (p in toProcess) {
                visited.add(p)
                area.add(p)
                nextToProcess.addAll(DIRECTIONS.map { it + p }.filter { grid.inGrid(it) && grid.at(it) == plantType }
                    .filterNot { area.contains(it) })
            }
            toProcess = nextToProcess
        }


        areas += area
    }

    return areas
}

fun areaPerimeter(area: Set<Vec2>): Long {
    return area.sumOf { pos -> 4 - DIRECTIONS.map { it + pos }.count { it in area } }.toLong()
}

fun solveLevel1(grid: Grid): Long {


    val areas: List<Set<Vec2>> = findAreas(grid)

    val totalPrice = areas.sumOf { area -> areaPerimeter(area) * area.size }

    println("Total price (part1): $totalPrice")
    return totalPrice
}

val CORNER_MATRIX = mapOf(
    Vec2(0, 0) to listOf(Vec2.LEFT, Vec2.UP_LEFT, Vec2.UP),
    Vec2(1, 0) to listOf(Vec2.UP, Vec2.UP_RIGHT, Vec2.RIGHT),
    Vec2(1, 1) to listOf(Vec2.RIGHT, Vec2.DOWN_RIGHT, Vec2.DOWN),
    Vec2(0, 1) to listOf(Vec2.DOWN, Vec2.DOWN_LEFT, Vec2.LEFT)
)

fun areaSides(area: Set<Vec2>): Long {
    val corners = mutableSetOf<Vec2>()
    var extra = 0
    for (pos in area) {
        if (DIRECTIONS.all { (pos + it) in area }) continue
        for ((offset, corner) in CORNER_MATRIX) {
            if (corner.count { (pos + it) !in area } % 2 == 1)
                corners.add(pos + offset)
            else if (listOf(corner.first(), corner.last()).all { (pos + it) !in area })
                if (!corners.contains(pos + offset)) corners.add(pos + offset)
                else extra++
        }
    }
    return corners.size.toLong() + extra
}

fun solveLevel2(grid: Grid): Long {


    val areas: List<Set<Vec2>> = measureTimedValue { findAreas(grid) }.let {
        println("Time to find areas: ${it.duration}")
        it.value
    }

    val totalPrice = areas.sumOf { area -> areaSides(area) * area.size }

    println("Total price (part 2): $totalPrice")
    return totalPrice
}

fun main() {
    val text = getInput(12)
    val grid = parseGrid(text)
    runLevels(12, { solveLevel1(grid) }, { solveLevel2(grid) })
}