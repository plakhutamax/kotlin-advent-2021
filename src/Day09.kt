
typealias BasinSize = Int

fun main() {

    data class Point(val x: Int, val y: Int, val value: Int)

    fun Point.isLowPoint(input: Array<IntArray>, knownPoints: List<Point> = emptyList()): Boolean {
        val width = input[0].size
        val height = input.size

        val adjacents = buildList {
            if (x > 0) add(Point(x - 1, y , input[y][x - 1]))
            if (y > 0) add(Point(x, y - 1 , input[y - 1][x]))
            if (x < width - 1) add(Point(x + 1, y , input[y][x + 1]))
            if (y < height - 1) add(Point(x, y + 1 , input[y + 1][x]))
        }
        return adjacents.all { it in knownPoints || it.value > value }

    }

    fun part1(input: Array<IntArray>): Int {
        val lowPoints = mutableListOf<Int>()
        input.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, point ->
                if (Point(colIndex, rowIndex, point).isLowPoint(input)) lowPoints.add(point)
            }
        }

        println(lowPoints)
        return lowPoints.fold(0) { acc, p -> acc + p + 1 }
    }


    fun dfs(i: Int, j: Int, input: Array<IntArray>, traversed: Array<BooleanArray>): Int {
        val width = input[0].size
        val height = input.size
        if (i < 0 || i >= height || j < 0 || j >= width || traversed[i][j] || input[i][j] == 9) return 0
        traversed[i][j] = true
        return 1 + dfs(i + 1, j, input, traversed) + dfs(i - 1, j, input, traversed) + dfs(i, j + 1, input, traversed) + dfs(i, j - 1, input, traversed)
    }

    fun part2(input: Array<IntArray>): Int {
        val width = input[0].size
        val height = input.size
        val traversed = Array(height) { BooleanArray(width) }
        val basin = mutableListOf<Int>()
        input.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, point ->
                if (!traversed[rowIndex][colIndex] && point != 9) basin.add(dfs(rowIndex, colIndex, input, traversed))
            }
        }

        return basin.sortedDescending().subList(0, 3).fold(1) { acc, r -> acc * r }
    }

    fun List<String>.to2dArray(): Array<IntArray> {
        val result = Array(size) { IntArray(first().length) }
        forEachIndexed { i, stringRow ->
            stringRow.forEachIndexed { index, c ->
                result[i][index] = Integer.parseInt(c.toString())
            }
        }
        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput.to2dArray()) == 15)
    check(part2(testInput.to2dArray()) == 1134)

    val input = readInput("Day09_input")
    println(part1(input.to2dArray()))
    println(part2(input.to2dArray()))
}
