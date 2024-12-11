package day11

import getResourceAsText
import java.util.LinkedList



fun main() {
    val text = getResourceAsText("/day11/input") ?: error("Input not found")


    val list = LinkedList<Long>()
    list.addAll(text.replace("\n", "").split(" ").map { it.toLong() })

    val STEPS = 25

    for(step in 0..<STEPS){

        val iter = list.listIterator()

        while (iter.hasNext()){
            val stone = iter.next()
            if(stone == 0L){
                iter.set(1L)
            }else if(stone.toString().length % 2 == 0){
                stone.toString().also {
                    it.substring(0..<it.length / 2).toLong().also { iter.set(it) }
                    it.substring((it.length / 2)..<it.length).toLong().also {
                        iter.add(it)
                        //iter.next()
                    }
                }
            } else {
                iter.set(stone * 2024L)
            }
        }

        println("Amount of stones after ${step + 1} steps: ${list.size}")
    }

    val stoneCount = list.size

    println("Number of stones: $stoneCount")

}