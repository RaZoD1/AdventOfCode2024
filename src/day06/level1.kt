package day06

import Vec2
import day04.at
import day04.inGrid
import getResourceAsText

fun findCharInGrid(grid: List<List<Char>>, char: Char): Vec2 {
    for((rowIdx, row) in grid.withIndex()){
        for((colIdx, c) in row.withIndex()){
            if(c == char) return Vec2(colIdx, rowIdx)
        }
    }
    error("Couldn't find char '$char' in grid")
}

fun charForDirection(dir: Vec2): Char = when{
        dir.row > 0 -> 'v'
        dir.row < 0 -> '^'
        dir.col > 0 -> '>'
        dir.col < 0 -> '<'
        else -> error("No direction for $dir")
    }


fun main(){
    val text = getResourceAsText("/day06/input") ?: error("Input not found")

    val grid = text.split("\n").filter { it.isNotEmpty() }.map { it.toMutableList() }

    val startPos = findCharInGrid(grid, '^')
    val startDir = Vec2.UP

    var pos = startPos
    var dir = startDir

    while(true){
        val nextPos = pos + dir
        if(!grid.inGrid(nextPos)) break

        if(grid.at(nextPos) == '#'){
            dir = dir.turnRight()
            continue
        }

        if(grid.at(nextPos) == '.'){
            grid[nextPos.row][nextPos.col] = charForDirection(dir)
        } else if(grid.at(nextPos) == charForDirection(dir)){
            println("Reached Loop")
            break // loop
        }
        pos = nextPos
    }

    val distinctVisitedTiles = grid.joinToString().count { "<>v^".contains(it) }

    println("Visited Tiles: $distinctVisitedTiles")
}