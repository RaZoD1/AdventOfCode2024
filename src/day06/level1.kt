package day06

import Vec2
import day04.at
import day04.inGrid
import getResourceAsText
import kotlin.time.measureTimedValue

fun findCharInGrid(grid: List<List<Char>>, char: Char): Vec2 {
    for((rowIdx, row) in grid.withIndex()){
        for((colIdx, c) in row.withIndex()){
            if(c == char) return Vec2(colIdx, rowIdx)
        }
    }
    error("Couldn't find char '$char' in grid")
}

fun findVisitedTiles(grid: List<List<Char>>, startPos: Vec2, startDir: Vec2 = Vec2.UP): Set<Vec2> {

    val visited = mutableSetOf<Pair<Vec2, Vec2>>(Pair(startPos, startDir)) // Pos,Dir

    var pos = startPos
    var dir = startDir

    while(true){
        val nextPos = pos + dir
        if(!grid.inGrid(nextPos)) break

        if(grid.at(nextPos) == '#'){
            dir = dir.turnRight()
            continue
        }

        val nextMove = Pair(nextPos, dir)
        if(visited.contains(nextMove)){
            break
        }
        visited.add(nextMove)
        pos = nextPos
    }
    return visited.map { it.first }.toSet()
}


fun main(){
    val text = getResourceAsText("/day06/input") ?: error("Input not found")

    val grid = text.split("\n").filter { it.isNotEmpty() }.map { it.toMutableList() }

    val startPos = findCharInGrid(grid, '^')

    val distinctVisitedTiles = measureTimedValue { findVisitedTiles(grid, startPos) }.let {
        println("Time (Part 1): ${it.duration.inWholeMilliseconds}ms")
        it.value
    }

    println("Visited Tiles: ${distinctVisitedTiles.size}")
}