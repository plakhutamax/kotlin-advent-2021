import kotlin.math.absoluteValue

data class Rectangle(val xRange: IntRange, val yRange: IntRange) {
    fun contains(x: Int, y: Int): Boolean {
        return x in xRange && y in yRange
    }

    fun missed(x: Int, y: Int): Boolean {
        return x > xRange.last && y < yRange.last
    }
}

fun main() {

    val highestPoint = mutableSetOf<Int>()
    var count = 0

    fun simulate(startXVel: Int, startYVel: Int, rect: Rectangle) {
        var xvel = startXVel
        var yvel = startYVel
        var xPos = 0
        var yPos = 0

        var highestY = Int.MIN_VALUE


        fun step() {
            xPos += xvel
            yPos += yvel
            highestY = maxOf(highestY, yPos)
            if (xvel > 0) xvel-- else xvel++
            yvel--
        }

        for (i in 0..1000) {
            step()
            if (rect.contains(xPos, yPos)) {
                println("Found pair ($startXVel, $startYVel)")
                highestPoint.add(highestY)
                count++
                break
            }


//            if (rect.missed(xPos, yPos)) break
        }
    }

    fun part1(target: Rectangle): Int {
        for (x in 0..200) {
            for (y in -200..200) {
//                println("$x, $y")
                simulate(x, y, target)
            }
        }
        return highestPoint.maxOrNull()!!
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    fun readTargetRectangle(s: String): Rectangle {
        val area = s.split('=', ',')
            .flatMap { it.split(" ") }
            .filter { it.contains("..") }
        val (x1, x2) = area.first().split("..").map(String::toInt)
        val (y1, y2) = area.last().split("..").map(String::toInt)

        return Rectangle(x1..x2, y1..y2)
    }

    val rect = readTargetRectangle(readInput("Day17_test").first())
    var h = part1(rect)
    println(count)
    println(h)
}
