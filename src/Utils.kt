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
    operator fun plus(v: Vec2): Vec2 {
        return Vec2(this.col + v.col, this.row + v.row)
    }
    operator fun minus(v: Vec2): Vec2 {
        return Vec2(this.col - v.col, this.row - v.row)
    }
    operator fun times(scale: Int): Vec2 {
        return Vec2(this.col * scale, this.row*scale)
    }
}