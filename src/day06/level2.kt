package day06

import Vec2
import day04.at
import day04.inGrid
import getResourceAsText
import kotlin.time.measureTimedValue

fun isLoop(grid: List<List<Char>>, extraBlock: Vec2, startPos: Vec2, startDir: Vec2 = Vec2.UP): Boolean {

    val visited = mutableSetOf<Pair<Vec2, Vec2>>(Pair(startPos, startDir)) // Pos,Dir

    var pos = startPos
    var dir = startDir

    while(true){
        val nextPos = pos + dir
        if(!grid.inGrid(nextPos)) return false

        if(grid.at(nextPos) == '#' || nextPos == extraBlock){
            dir = dir.turnRight()
            continue
        }

        val nextMove = Pair(nextPos, dir)
        if(visited.contains(nextMove)){
            return true
        }
        visited.add(nextMove)
        pos = nextPos
    }
}

fun main(){
    val text = getResourceAsText("/day06/input") ?: error("Input not found")

    val grid = text.split("\n").filter { it.isNotEmpty() }.map { it.toList() }

    val startPos = findCharInGrid(grid, '^')

    val allPositions = grid.indices.flatMap { rowIdx -> grid[rowIdx].indices.map { colIdx -> Vec2(colIdx, rowIdx) } }
    val possibleBlocks = allPositions.parallelStream().filter { isLoop(grid, it, startPos) }.count()


    println("Possible Blocks: $possibleBlocks")
}