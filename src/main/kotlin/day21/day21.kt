package day21

import Vec2
import getInput
import runLevels
import kotlin.math.abs
import kotlin.math.sign


enum class Button(val char: Char) {
    ACTIVATE('A'),
    ZERO('0'),
    ONE('1'),
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9'),
    UP('^'),
    LEFT('<'),
    RIGHT('>'),
    DOWN('v');

    companion object {
        private val lookupMap by lazy { mutableMapOf<Char, Button>() }

        init {
            // Populate the lookupMap once the enum constants are fully initialized
            for (button in entries) {
                lookupMap[button.char] = button
            }
        }

        fun lookup(char: Char): Button? = lookupMap[char]
    }
}


val KEYPAD = mapOf<Button, Vec2>(
    Button.SEVEN to Vec2(0, 0), Button.EIGHT to Vec2(1, 0), Button.NINE to Vec2(2, 0),
    Button.FOUR to Vec2(0, 1), Button.FIVE to Vec2(1, 1), Button.SIX to Vec2(2, 1),
    Button.ONE to Vec2(0, 2), Button.TWO to Vec2(1, 2), Button.THREE to Vec2(2, 2),
    Button.ZERO to Vec2(1, 3), Button.ACTIVATE to Vec2(2, 3)
)
val KEYPAD_VALUES = KEYPAD.values.toSet()

val D_PAD = mapOf<Button, Vec2>(
    Button.UP to Vec2(1, 0), Button.ACTIVATE to Vec2(2, 0),
    Button.LEFT to Vec2(0, 1), Button.DOWN to Vec2(1, 1), Button.RIGHT to Vec2(2, 1)
)
val D_PAD_VALUES = D_PAD.values.toSet()

data class Node(val move: Button, val nextMoves: List<Node>)


sealed class KeypadController {
    class Manual(): KeypadController() {
        override fun findCost(buttons: List<Button>): Long {
            return buttons.size.toLong()
        }
    }

    class Robot(val controlledBy: KeypadController): KeypadController() {
        override fun findCost(buttons: List<Button>): Long {
            return (listOf(Button.ACTIVATE) + buttons)
                .map { button -> D_PAD[button]!! }
                .zipWithNext()
                .sumOf { (from, to) ->  fromToHitCost(from, to)}
        }
        fun solveDiagonal(from: Vec2, delta: Vec2): Long {

            val possibilities = mutableListOf<Pair<Vec2, Vec2>>()

            val dCol = delta.copy(row = 0)
            val dRow = delta.copy(col = 0)

            if(from + dRow in D_PAD_VALUES){
                possibilities += Pair(dRow, dCol)
            }
            if(from + dCol in D_PAD_VALUES){
                possibilities += Pair(dCol, dRow)
            }

            return possibilities.minOf { order ->
                val buttonsToHit = listOf(dirToBtn.getValue(order.first.sign()),dirToBtn.getValue(order.second.sign()), Button.ACTIVATE)
                return@minOf controlledBy.findCost(buttonsToHit) + (abs(delta.x) + abs(delta.y) - 2)
            }
        }

        fun solveStraight(from: Vec2, delta: Vec2): Long {
            val buttonsToHit = listOf(dirToBtn.getValue(delta.sign()), Button.ACTIVATE)
            return controlledBy.findCost(buttonsToHit) + (abs(delta.x) + abs(delta.y) - 1)
        }

        val cache = mutableMapOf<Pair<Vec2, Vec2>, Long>()
        fun fromToHitCost(from: Vec2, to: Vec2): Long {
            val cacheKey = Pair(from, to)
            return cache.getOrPut(cacheKey) {
                val delta = to - from
                val (dx, dy) = delta
                if(dx != 0 && dy != 0){
                    solveDiagonal(from, delta)
                } else {
                    solveStraight(from, delta)
                }
            }
        }
    }

    abstract fun findCost(buttons: List<Button>): Long


}

class Keypad(val controlledBy: KeypadController) {

    fun findCost(buttons: List<Button>): Long {
       return (listOf(Button.ACTIVATE) + buttons)
           .map { button -> KEYPAD[button]!! }
           .zipWithNext()
           .sumOf { (from, to) ->  fromToHitCost(from, to)}
    }

    fun solveDiagonal(from: Vec2, delta: Vec2): Long {

        val possibilities = mutableListOf<Pair<Vec2, Vec2>>()

        val dCol = delta.copy(row = 0)
        val dRow = delta.copy(col = 0)

        if(from + dRow in KEYPAD_VALUES){
            possibilities += Pair(dRow, dCol)
        }
        if(from + dCol in KEYPAD_VALUES){
            possibilities += Pair(dCol, dRow)
        }

        return possibilities.minOf { order ->
            val buttonsToHit = listOf(dirToBtn.getValue(order.first.sign()),dirToBtn.getValue(order.second.sign()), Button.ACTIVATE)
            return@minOf controlledBy.findCost(buttonsToHit) + (abs(delta.x) + abs(delta.y) - 2)
        }
    }

    fun solveStraight(from: Vec2, delta: Vec2): Long {
        val buttonsToHit = listOf(dirToBtn.getValue(delta.sign()), Button.ACTIVATE)
        return controlledBy.findCost(buttonsToHit) + (abs(delta.x) + abs(delta.y) - 1)
    }

    fun fromToHitCost(from: Vec2, to: Vec2): Long {
        val delta = to - from
        val (dx, dy) = delta
        return if(dx != 0 && dy != 0){
            solveDiagonal(from, delta)
        } else {
            solveStraight(from, delta)
        }
    }
}

fun Vec2.sign(): Vec2 = Vec2(this.col.sign, this.row.sign)

val dirToBtn = mapOf<Vec2, Button>(
    Vec2.LEFT to Button.LEFT,
    Vec2.RIGHT to Button.RIGHT,
    Vec2.UP to Button.UP,
    Vec2.DOWN to Button.DOWN
)

fun findCodeComplexity(code: String, keypad: Keypad): Long {
    val numericCodePart = code.takeWhile { c -> c.isDigit() }.toLong()
    val buttons = code.map { c -> Button.lookup(c) ?: error("Code contains unknown Button $c") }



    val typingCost = keypad.findCost(buttons)

    println("\nFor code $code: Cost $typingCost  Numeric Part $numericCodePart")
    return numericCodePart * typingCost
}

fun solveLevel1(codes: List<String>): Long {
    val manualController = KeypadController.Manual()
    val c1 = KeypadController.Robot(manualController)
    val c2 = KeypadController.Robot(c1)
    val keypad = Keypad(c2)

    return codes.sumOf {findCodeComplexity(it, keypad)}
}


fun solveLevel2(codes: List<String>): Long {
    val manualController = KeypadController.Manual()
    var controlledBy: KeypadController = manualController

    for(i in 1..25){
        controlledBy = KeypadController.Robot(controlledBy)
    }

    val keypad = Keypad(controlledBy)

    return codes.sumOf {findCodeComplexity(it, keypad)}
}


const val USE_SAMPLE = false

fun main() {
    val text = getInput(21, USE_SAMPLE)

    val codes = text.split("\n").filter { it.isNotBlank() }

    runLevels(
        21,
        { solveLevel1(codes) },
        { solveLevel2(codes) },
        times = 1
    )
}