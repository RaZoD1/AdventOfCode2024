package day10

import Vec2
import day04.at
import day04.inGrid
import day08.Grid
import getResourceAsText


val DIRECTIONS = listOf(Vec2.UP, Vec2.DOWN, Vec2.RIGHT, Vec2.LEFT)

fun countTrailheadScore(grid: Grid, startPos: Vec2): Int {

    var trackedPositions = mutableSetOf(startPos)
    var nextPositions = mutableSetOf<Vec2>()
    val finalPositions = mutableSetOf<Vec2>()
    while(trackedPositions.isNotEmpty()){

        trackedPositions.forEach { pos ->
            val height = grid.at(pos).digitToInt()
            if(height == 9) finalPositions.add(pos)
            for (dir in DIRECTIONS) {
                val nextPosition = pos + dir
                if(!grid.inGrid(nextPosition)) continue
                if(grid.at(nextPosition).digitToInt() == (height + 1)){
                    nextPositions.add(nextPosition)
                }
            }
        }
        trackedPositions = nextPositions
        nextPositions = mutableSetOf()
    }


    return finalPositions.size
}

fun main() {
    val text = getResourceAsText("/day10/input") ?: error("Input not found")

    val grid = text.split("\n").filter { it.isNotEmpty() }.map { it.toList() }

    val startPositions: List<Vec2> = grid.mapIndexed { rowIdx, row -> row.mapIndexed { colIdx, c ->
        val pos = Vec2(colIdx, rowIdx)
        if(c == '0') pos else null
    }.filterNot { it == null } }.flatten() as List<Vec2>

    val totalScore = startPositions.sumOf { countTrailheadScore(grid, it) }

    println("Total score: $totalScore")


}