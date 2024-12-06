package day06

import Vec2
import day04.at
import day04.inGrid
import getResourceAsText
import kotlin.time.measureTime

fun isLoop(grid: List<List<Char>>, extraBlock: Vec2, startPos: Vec2, startDir: Vec2 = Vec2.UP): Boolean {

    val visitedTurns = mutableSetOf<Pair<Vec2, Vec2>>() // Pos,Dir

    var pos = startPos
    var dir = startDir

    while(true){
        val nextPos = pos + dir
        if(!grid.inGrid(nextPos)) return false

        if(grid.at(nextPos) == '#' || nextPos == extraBlock){

            val lastMove = Pair(pos, dir)
            if(visitedTurns.contains(lastMove)){
                return true
            }
            visitedTurns.add(lastMove)

            dir = dir.turnRight()
            continue
        }
        pos = nextPos
    }
}

fun bruteForceParallel(grid: List<List<Char>>){
    val startPos = findCharInGrid(grid, '^')

    val visitedTiles = findVisitedTiles(grid, startPos)
    val possibleBlocks = visitedTiles.parallelStream().filter { isLoop(grid, it, startPos) }.count()
    println("Possible Blocks (brute force - parallel): $possibleBlocks")

}

fun bruteForce(grid: List<List<Char>>){
    val startPos = findCharInGrid(grid, '^')

    val visitedTiles = findVisitedTiles(grid, startPos)
    val possibleBlocks = visitedTiles.count { isLoop(grid, it, startPos) }
    println("Possible Blocks (brute force): $possibleBlocks")
}

fun main(){
    val text = getResourceAsText("/day06/input") ?: error("Input not found")
    val grid = text.split("\n").filter { it.isNotEmpty() }.map { it.toList() }

    measureTime { bruteForce(grid) }.also { println("Brute force time (synchronous): ${it.inWholeMilliseconds}ms") }
    measureTime { bruteForceParallel(grid) }.also { println("Brute force time (parallel): ${it.inWholeMilliseconds}ms") }
}