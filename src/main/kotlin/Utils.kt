import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.math.sin

fun getResourceAsText(path: String): String? =
    object {}.javaClass.getResource(path)?.readText()

class InputFile(val path: String){
    private val rawText = getResourceAsText(path);
    init {
        if(rawText == null ) throw Exception("Resource $path is null")
    }

    val text = rawText!!.trim().replace("\r", "")
    val lines = text.split("\n").filterNot {it.isEmpty()}
}

abstract class Level(val path: String){

    val inputFile = InputFile(path)

    fun start(){
        onInit()
        onText(inputFile.text)
        onLines(inputFile.lines)
        inputFile.lines.forEach { onLine(it) }
        println(onResult())
    }

    open fun onInit(){}
    open fun onText(text: String){}
    open fun onLines(lines: List<String>){}
    open fun onLine(line: String){}

    abstract fun onResult(): String
}

data class Vec2(val col: Int, val row: Int){
    companion object{
        val LEFT = Vec2(-1, 0)
        val RIGHT = Vec2(1, 0)
        val UP = Vec2(0, -1)
        val DOWN = Vec2(0, 1)
        val UP_LEFT = Vec2(-1, -1)
        val UP_RIGHT = Vec2(1, -1)
        val DOWN_LEFT = Vec2(-1, 1)
        val DOWN_RIGHT = Vec2(1, 1)
    }

    val x = col
    val y = row

    operator fun plus(v: Vec2): Vec2 {
        return Vec2(this.col + v.col, this.row + v.row)
    }
    operator fun minus(v: Vec2): Vec2 {
        return Vec2(this.col - v.col, this.row - v.row)
    }
    operator fun times(scale: Int): Vec2 {
        return Vec2(this.col * scale, this.row*scale)
    }

    fun rotate(degrees: Double): Vec2 {

        val radians = Math.toRadians(degrees)
        return Vec2(
            row = (sin(radians) * col + cos(radians) * row).roundToInt(),
            col = (cos(radians) * col - sin(radians) * row).roundToInt()
        )
    }
    fun turnRight(): Vec2 {
        return this.rotate(90.0)
    }

    fun turnLeft(): Vec2 {
        return this.rotate(-90.0)
    }

    fun turnAround(): Vec2 {
        return this.rotate(180.0)
    }
}


data class LVec2(val col: Long, val row: Long){
    companion object{
        val LEFT = LVec2(-1, 0)
        val RIGHT = LVec2(1, 0)
        val UP = LVec2(0, -1)
        val DOWN = LVec2(0, 1)
        val UP_LEFT = LVec2(-1, -1)
        val UP_RIGHT = LVec2(1, -1)
        val DOWN_LEFT = LVec2(-1, 1)
        val DOWN_RIGHT = LVec2(1, 1)
    }

    val x = col
    val y = row

    operator fun plus(v: LVec2): LVec2 {
        return LVec2(this.col + v.col, this.row + v.row)
    }
    operator fun minus(v: LVec2): LVec2 {
        return LVec2(this.col - v.col, this.row - v.row)
    }
    operator fun times(scale: Long): LVec2 {
        return LVec2(this.col * scale, this.row*scale)
    }

    fun rotate(degrees: Double): LVec2 {

        val radians = Math.toRadians(degrees)
        return LVec2(
            row = (sin(radians) * col + cos(radians) * row).roundToLong(),
            col = (cos(radians) * col - sin(radians) * row).roundToLong()
        )
    }
    fun turnRight(): LVec2 {
        return this.rotate(90.0)
    }

    fun turnLeft(): LVec2 {
        return this.rotate(-90.0)
    }

    fun turnAround(): LVec2 {
        return this.rotate(180.0)
    }
}

fun calculateLCM(a: Long, b: Long): Long {
    if (a == 0L || b == 0L) {
        throw IllegalArgumentException("LCM is not defined for zero.")
    }

    return (a / gcd(a, b)) * b
}

// Helper function to calculate GCD using the Euclidean algorithm
fun gcd(a: Long, b: Long): Long {
    var x = a
    var y = b

    while (y != 0L) {
        val temp = y
        y = x % y
        x = temp
    }

    return x
}

fun plotVec2s(positions: Iterable<Vec2>) {
    val cols = positions.maxOf { it.col } + 1
    val rows = positions.maxOf { it.row } + 1
    val grid = List(rows) { MutableList(cols) {'.'} }
    for (position in positions) {
        grid[position.row][position.col] = 'X'
    }
    println(grid.joinToString("\n") { it.joinToString("") })

}