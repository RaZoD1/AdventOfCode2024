package day04

import Vec2
import getResourceAsText


val DIAG_DIRECTIONS = listOf(Vec2.UP_LEFT, Vec2.UP_RIGHT, Vec2.DOWN_LEFT, Vec2.DOWN_RIGHT)

fun msToggle(c: Char): Char = if(c == 'M') 'S' else 'M'

fun main(){
    val text = getResourceAsText("/day04/input") ?: error("Input not found")

    val grid = text.split("\n").map { it.toCharArray().toList() }

    var xmasCount = 0

    for(rowIdx in grid.indices){
        cols@for(colIdx in grid[rowIdx].indices){
            val pos = Vec2(col = colIdx, row = rowIdx)

            if(grid[rowIdx][colIdx] != 'A') continue

            for(dir in DIAG_DIRECTIONS){
                if(!grid.inGrid(pos + dir)) continue@cols
            }
            val upLeftChar = grid.at(pos + Vec2.UP_LEFT)
            if(upLeftChar == 'M' || upLeftChar == 'S'){
                if(msToggle(upLeftChar) != grid.at(pos+Vec2.DOWN_RIGHT)) continue@cols
            } else continue@cols
            val upRightChar = grid.at(pos + Vec2.UP_RIGHT)
            if(upRightChar=='M' || upRightChar=='S'){
                if(msToggle(upRightChar) != grid.at(pos+Vec2.DOWN_LEFT)) continue@cols
            } else continue@cols

            xmasCount++
        }
    }

    println("Amount of X-MAS: $xmasCount")


}