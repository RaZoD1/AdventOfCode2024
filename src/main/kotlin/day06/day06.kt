package day06

import Vec2
import day04.at
import day04.inGrid
import day07.solveLevel1
import day07.solveLevel2
import day08.Grid
import getInput
import runLevels
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

fun findCharInGrid(grid: List<List<Char>>, char: Char): Vec2 {
    for ((rowIdx, row) in grid.withIndex()) {
        for ((colIdx, c) in row.withIndex()) {
            if (c == char) return Vec2(colIdx, rowIdx)
        }
    }
    error("Couldn't find char '$char' in grid")
}

fun findVisitedTiles(grid: List<List<Char>>, startPos: Vec2, startDir: Vec2 = Vec2.UP): Set<Vec2> {

    val visited = mutableSetOf<Pair<Vec2, Vec2>>(Pair(startPos, startDir)) // Pos,Dir

    var pos = startPos
    var dir = startDir

    while (true) {
        val nextPos = pos + dir
        if (!grid.inGrid(nextPos)) break

        if (grid.at(nextPos) == '#') {
            dir = dir.turnRight()
            continue
        }

        val nextMove = Pair(nextPos, dir)
        if (visited.contains(nextMove)) {
            break
        }
        visited.add(nextMove)
        pos = nextPos
    }
    return visited.map { it.first }.toSet()
}


fun solveLevel1(grid: Grid): Long {
    val startPos = findCharInGrid(grid, '^')

    val distinctVisitedTiles = measureTimedValue { findVisitedTiles(grid, startPos) }.let {
        println("Time (Part 1): ${it.duration.inWholeMilliseconds}ms")
        it.value
    }

    println("Visited Tiles: ${distinctVisitedTiles.size}")
    return distinctVisitedTiles.size.toLong()
}

fun isLoop(grid: List<List<Char>>, extraBlock: Vec2, startPos: Vec2, startDir: Vec2 = Vec2.UP): Boolean {

    val visitedTurns = mutableSetOf<Pair<Vec2, Vec2>>() // Pos,Dir

    var pos = startPos
    var dir = startDir

    while (true) {
        val nextPos = pos + dir
        if (!grid.inGrid(nextPos)) return false

        if (grid.at(nextPos) == '#' || nextPos == extraBlock) {

            val lastMove = Pair(pos, dir)
            if (visitedTurns.contains(lastMove)) {
                return true
            }
            visitedTurns.add(lastMove)

            dir = dir.turnRight()
            continue
        }
        pos = nextPos
    }
}


fun bruteForceParallel(grid: List<List<Char>>): Long {
    val startPos = findCharInGrid(grid, '^')

    val visitedTiles = findVisitedTiles(grid, startPos)
    val possibleBlocks = visitedTiles.parallelStream().filter { isLoop(grid, it, startPos) }.count()
    println("Possible Blocks (brute force - parallel): $possibleBlocks")
    return possibleBlocks
}

fun bruteForce(grid: List<List<Char>>): Long{
    val startPos = findCharInGrid(grid, '^')

    val visitedTiles = findVisitedTiles(grid, startPos)
    val possibleBlocks = visitedTiles.count { isLoop(grid, it, startPos) }
    println("Possible Blocks (brute force): $possibleBlocks")
    return possibleBlocks.toLong()
}

fun solveLevel2(grid: Grid): Long {
    return bruteForceParallel(grid)
}

fun main() {
    val text = getInput(6)

    val grid = text.split("\n").filter { it.isNotEmpty() }.map { it.toList() }
    runLevels(6, { solveLevel1(grid) }, { solveLevel2(grid) })
}