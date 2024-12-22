package day16

import Grid
import Vec2
import at
import findCharInGrid
import getInput
import inGrid
import parseGrid
import runLevels
import java.util.*


const val START = 'S'
const val GOAL = 'E'
const val WALL = '#'
const val EMPTY = '.'

fun getNeighbors(grid: Grid, current: Pair<Vec2, Vec2>): Map<Pair<Vec2, Vec2>, Long> {
    val neighbors = mutableMapOf<Pair<Vec2, Vec2>, Long>()

    val nextPos = current.first + current.second
    if (grid.inGrid(nextPos) && grid.at(nextPos) != WALL) neighbors[nextPos to current.second] = 1
    neighbors[current.first to current.second.turnLeft()] = 1000
    neighbors[current.first to current.second.turnRight()] = 1000
    return neighbors
}

fun solveLevel1(grid: Grid): Long {

    val startPos = findCharInGrid(grid, START)
    val goalPos = findCharInGrid(grid, GOAL)

    val scores = mutableMapOf<Pair<Vec2, Vec2>, Long>() // position, direction -> Score
    val queue = PriorityQueue<Pair<Vec2, Vec2>>(compareBy { t -> scores.getOrElse(t) { Long.MAX_VALUE } })

    queue.add(startPos to Vec2.RIGHT)
    scores[startPos to Vec2.RIGHT] = 0L

    while (queue.isNotEmpty()) {
        val cur = queue.remove()
        val curScore = scores[cur] ?: error("Not in scores")


        for ((key, deltaScore) in getNeighbors(grid, cur)) {
            if (key !in scores) {
                scores[key] = curScore + deltaScore
                queue.add(key)
                continue
            }
            if (scores[key]!! > curScore + deltaScore) {
                scores[key] = curScore + deltaScore
                queue.add(key)
            }
        }
    }


    return scores.filter { entry -> entry.key.first == goalPos }.values.min()
}


fun solveLevel2(grid: Grid): Long {
    val startPos = findCharInGrid(grid, START)
    val goalPos = findCharInGrid(grid, GOAL)

    val scores = mutableMapOf<Pair<Vec2, Vec2>, Long>() // position, direction -> Score
    val queue = PriorityQueue<Pair<Vec2, Vec2>>(compareBy { t -> scores.getOrElse(t) { Long.MAX_VALUE } })
    val parents = mutableMapOf<Pair<Vec2, Vec2>, MutableList<Pair<Vec2, Vec2>>>()


    queue.add(startPos to Vec2.RIGHT)
    scores[startPos to Vec2.RIGHT] = 0L

    while (queue.isNotEmpty()) {
        val cur = queue.remove()
        val curScore = scores[cur] ?: error("Not in scores")


        for ((key, deltaScore) in getNeighbors(grid, cur)) {
            if (key !in scores) {
                scores[key] = curScore + deltaScore
                parents[key] = mutableListOf(cur)
                queue.add(key)
                continue
            }
            if (scores[key]!! > curScore + deltaScore) {
                scores[key] = curScore + deltaScore
                parents[key]!!.let {
                    it.clear()
                    it.add(cur)
                }

                queue.add(key)
            }
            if (scores[key]!! == curScore + deltaScore) {
                parents[key]!!.add(cur)
            }
        }
    }

    val endKey = scores.filter { entry -> entry.key.first == goalPos }.minBy { entry -> entry.value }.key

    val tiles = mutableSetOf<Vec2>()
    val keys = mutableSetOf<Pair<Vec2, Vec2>>(endKey)
    while (keys.isNotEmpty()) {
        val key = keys.first().also { keys.remove(it) }
        tiles.add(key.first)
        keys.addAll(parents[key] ?: emptySet())
    }


    return tiles.size.toLong()
}

fun main() {
    val text = getInput(16)
    val grid = text.split("\n\n").first().let { parseGrid(it) }


    runLevels(16, { solveLevel1(grid) }, { solveLevel2(grid) }, times = 100)
}
