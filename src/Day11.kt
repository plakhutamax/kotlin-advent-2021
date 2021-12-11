fun main() {

    class Octopus(level: Int) {

        var energyLevel = level
        private var flashedOnStep = false

        fun doStep() {
            energyLevel++
        }

        fun increaseEnergyLevel() {

        }

        fun flash(): Boolean {
            if (energyLevel > 9 && !flashedOnStep) {
                flashedOnStep = true
                energyLevel = 0
                return true
            }

            return false
        }

        override fun toString(): String {
            return energyLevel.toString()
        }
    }

    fun List<String>.getOctopusGrid(): Array<Array<Octopus>> {
        val grid = Array(10) { Array<Octopus>(10) { Octopus(0) } }
        forEachIndexed { row, s ->
            s.forEachIndexed { col, e ->
                grid[row][col].energyLevel = Integer.parseInt(e.toString())
            }
        }
        return grid
    }

    fun List<String>.getGrid(): Array<IntArray> {
        val grid = Array(10) { IntArray(10) }
        forEachIndexed { row, s ->
            s.forEachIndexed { col, e ->
                grid[row][col] = Integer.parseInt(e.toString())
            }
        }
        return grid
    }

    data class Point(val x: Int, val y: Int)

    fun getAdjacentFor(i: Int, j: Int): List<Point> {
        val res = mutableListOf<Point>()
        if (i > 0) res.add(Point(j, i - 1))
        if (i > 0 && j > 0) res.add(Point(j - 1, i - 1))
        if (i > 0 && j < 9) res.add(Point(j + 1, i - 1))
        if (j > 0) res.add(Point(j - 1, i))
        if (j < 9) res.add(Point(j + 1, i))
        if (i < 9 && j > 0) res.add(Point(j - 1, i + 1))
        if (i < 9) res.add(Point(j, i + 1))
        if (i < 9 && j < 9) res.add(Point(j + 1, i + 1))
        return res
    }

    fun Array<Array<Octopus>>.getAdjacentFor(i: Int, j: Int): List<Octopus> {
        val res = mutableListOf<Octopus>()
        if (i > 0) res.add(this[i - 1][j])
        if (i > 0 && j > 0) res.add(this[i - 1][j - 1])
        if (i > 0 && j < 9) res.add(this[i - 1][j + 1])
        if (j > 0) res.add(this[i][j - 1])
        if (j < 9) res.add(this[i][j + 1])
        if (i < 9 && j > 0) res.add(this[i + 1][j - 1])
        if (i < 9) res.add(this[i + 1][j])
        if (i < 9 && j < 9) res.add(this[i + 1][j + 1])
        return res
    }

    fun Array<Array<Octopus>>.print() {
        forEach {
            it.forEach { o -> print("${o.energyLevel} ") }
            println()
        }
        println()
        println()
    }

    fun Array<IntArray>.print() {
        forEach {
            it.forEach { o -> print("$o ") }
            println()
        }
        println()
        println()
    }

    fun part1(input: Array<IntArray>): Int {
        var flashCount = 0
        for (step in 0..99) {
            val flashedOctopuses = mutableListOf<Point>()
            input.forEachIndexed { i, row ->
                row.forEachIndexed { j, level ->
                    val p = Point(j, i)
                    val toCheck = mutableListOf(p)
                    while (toCheck.isNotEmpty()) {
                        val oct = toCheck.removeFirst()
                        input[oct.y][oct.x] = input[oct.y][oct.x] + 1
                        if (input[oct.y][oct.x] > 9 && !flashedOctopuses.contains(Point(oct.x, oct.y))) {
                            flashedOctopuses.add(oct)
                            toCheck.addAll(getAdjacentFor(oct.y, oct.x))
                        }
                    }
                }
            }
            flashedOctopuses.forEach { input[it.y][it.x] = 0 }
            flashCount += flashedOctopuses.size
        }
        println(flashCount)
        return flashCount
    }

    fun part2(input: Array<IntArray>): Int {
        var step = 0
        while (true) {
            step++
            val flashedOctopuses = mutableListOf<Point>()
            input.forEachIndexed { i, row ->
                row.forEachIndexed { j, level ->
                    val p = Point(j, i)
                    val toCheck = mutableListOf(p)
                    while (toCheck.isNotEmpty()) {
                        val oct = toCheck.removeFirst()
                        input[oct.y][oct.x] = input[oct.y][oct.x] + 1
                        if (input[oct.y][oct.x] > 9 && !flashedOctopuses.contains(Point(oct.x, oct.y))) {
                            flashedOctopuses.add(oct)
                            toCheck.addAll(getAdjacentFor(oct.y, oct.x))
                        }
                    }
                }
            }
            flashedOctopuses.forEach { input[it.y][it.x] = 0 }
            println("Step ${step+1}")
            input.print()
            if (flashedOctopuses.size == 100) {
                println(step)
                return step
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test").getGrid()
    check(part1(testInput.copyOf()) == 1656)
    check(part2(testInput.copyOf()) == 195)

    val input = readInput("Day11_input").getGrid()
    println(part1(input.copyOf()))
    println(part2(input.copyOf()))
}
