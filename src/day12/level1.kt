package day12

import Vec2
import day04.at
import day04.inGrid
import day08.Grid
import getResourceAsText
import kotlin.time.measureTime

val DIRECTIONS = listOf(Vec2.UP, Vec2.DOWN, Vec2.RIGHT, Vec2.LEFT)


fun Grid.forEachCell(action: ((Vec2)-> Unit)): Unit {
    for(row in this.indices){
        for(col in this[row].indices){
            action(Vec2(col, row))
        }
    }
}

fun findAreas(grid: Grid): List<Set<Vec2>> {
    val visited = mutableSetOf<Vec2>()
    val areas = mutableListOf<Set<Vec2>>()

    grid.forEachCell { pos ->
        if(visited.contains(pos))
            return@forEachCell
        else
            visited.add(pos)
        val plantType = grid.at(pos)


        val area = mutableSetOf<Vec2>()
        var toProcess = mutableSetOf<Vec2>(pos)
        while(toProcess.isNotEmpty()){
            val nextToProcess = mutableSetOf<Vec2>()
            for(p in toProcess){
                visited.add(p)
                area.add(p)
                nextToProcess.addAll(DIRECTIONS.map { it + p }.filter { grid.inGrid(it) && grid.at(it) == plantType }.filterNot { area.contains(it) })
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

fun solveLevel1(text: String) {
    val grid = text.split("\n").filter { it.isNotEmpty() }.map { it.toList() }

    val areas: List<Set<Vec2>> = findAreas(grid)

    val totalPrice = areas.sumOf { area -> areaPerimeter(area)* area.size }

    println("Total price (part1): $totalPrice")
}

fun main() {
    val text = getResourceAsText("/day12/input") ?: error("Input not found")
    measureTime { solveLevel1(text) }.also {
        println("Time: $it")
    }
}