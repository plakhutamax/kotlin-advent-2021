enum class Axis {
    Horizontal, Vertical
}

data class Instruction(
    val axis: Axis,
    val coord: Int
)

data class Paper(
    val paper: Array<IntArray>,
    val instructions: List<Instruction>
)

fun main() {

    fun Array<IntArray>.foldBy(instruction: Instruction): Array<IntArray> {
        val folded = when (instruction.axis) {
            Axis.Horizontal -> Array(size - instruction.coord - 1) { IntArray(get(0).size) }
            Axis.Vertical -> Array(size) { IntArray(get(0).size - instruction.coord - 1) }
        }

        if (instruction.axis == Axis.Horizontal) {
            for (r in 0 until instruction.coord) {
                val row = get(r)
                for (c in row.indices) {
                    folded[r][c] = this[r][c]
                }
            }

            var ind = 1
            while (instruction.coord + ind != size) {
                val row = this[instruction.coord + ind]
                for (c in row.indices) {
                    folded[instruction.coord - ind][c] = this[instruction.coord - ind][c] or this[instruction.coord + ind][c]
                }
                ind++
            }
        } else {
            for (r in indices) {
                for (c in 0 until instruction.coord) {
                    folded[r][c] = this[r][c]
                }
            }

            val diff = this[0].size - instruction.coord
            for (d in 1 until diff) {
                for (r in indices) {
                    folded[r][instruction.coord - d] = this[r][instruction.coord - d] or this[r][instruction.coord + d]
                }
            }
        }

        return folded
    }

    fun part1(input: Paper): Int {
        val instruction = input.instructions.first()
        val f = input.paper.foldBy(instruction)
        var c = 0
        for (row in f) {
            c += row.sum()
        }
        return c
    }

    fun part2(input: Paper): Array<IntArray>  {
        return input.instructions.fold(input.paper) { paper, instruction -> paper.foldBy(instruction) }
    }

    fun readPaper(input: List<String>): Paper {
        val paperDots = mutableListOf<List<Int>>()
        var line = input.first()
        var maxX = 0
        var maxY = 0
        var index = 0
        while (line.isNotEmpty()) {
            val (x, y) = line.split(",").map(String::toInt)
            maxX = maxOf(maxX, x)
            maxY = maxOf(maxY, y)
            paperDots.add(listOf(x, y))
            index++
            line = input[index]
        }

        index++
        val instructions = mutableListOf<Instruction>()
        for (i in index until input.size) {
            val instructionLine = input[i]
            val (axis, coord) = instructionLine.split(" ")[2].split("=")
            instructions.add(
                Instruction(
                    if (axis == "y") Axis.Horizontal else Axis.Vertical,
                    coord.toInt()
                )
            )
        }
        val paper = Array(maxY + 1) { IntArray(maxX + 1) }
        paperDots.forEach { (x, y) -> paper[y][x] = 1 }
        return Paper(paper, instructions)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readPaper(readInput("Day13_test"))
    testInput.paper.forEach { y ->
        y.forEach { x ->
            print("$x ")
        }
        println()
    }
    check(part1(testInput) == 17)
//    check(part2(testInput) == 5)

    val input = part2(readPaper(readInput("Day13_input")))
    println(part1(readPaper(readInput("Day13_input"))))
    input.forEach { y ->
        y.forEach { x ->
            print(if (x == 1) "#" else ".")
        }
        println()
    }
//    println(part1(input))
//    println(part2(input))
}
