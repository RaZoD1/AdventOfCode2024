package day04

import Vec2
import getResourceAsText

val DIRECTIONS = listOf<Vec2>(Vec2.UP, Vec2.DOWN, Vec2.LEFT, Vec2.RIGHT, Vec2.UP_LEFT, Vec2.UP_RIGHT, Vec2.DOWN_LEFT, Vec2.DOWN_RIGHT)

fun <T>List<List<T>>.inGrid(pos: Vec2): Boolean {
    if(pos.row in this.indices){
        if(pos.col in this[pos.row].indices)
            return true
    }
    return false
}

fun <T>List<List<T>>.at(pos: Vec2): T {
    if(!this.inGrid(pos)) error("pos $pos outside of grid")

    return this[pos.row][pos.col]!!
}

fun main(){
    val text = getResourceAsText("/day04/input") ?: error("Input not found")

    val grid = text.split("\n").map { it.toCharArray().toList() }

    var xmasCount = 0

    for(rowIdx in grid.indices){
        for(colIdx in grid[rowIdx].indices){
            val pos = Vec2(col = colIdx, row = rowIdx)

            if(grid[rowIdx][colIdx] != 'X') continue

            directions@for(dir in DIRECTIONS){
                for(i in 1..3){
                    val letterPos = pos + (dir*i)
                    if(!grid.inGrid(letterPos)) continue@directions

                    if(grid.at(letterPos) != ("XMAS"[i])) continue@directions
                }
                xmasCount++

            }
        }
    }

    println("Amount of XMAS: $xmasCount")


}