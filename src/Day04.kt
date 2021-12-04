import java.lang.IllegalArgumentException

fun main() {

    class BingoBoard(
        remainingNumbers: List<Int>,
        val width: Int = 5,
        val height: Int = 5
    ) {
        val board = remainingNumbers.toMutableList()

        fun checkNumber(number: Int): Boolean {
            board.replaceAll {
                if (it == number) -1 else it
            }
            loop@ for (r in 0 until height) {
                for (c in 0 until width) {
                    print(board[r * width + c])
                    if (board[r * width + c] != -1) {
                        println()
                        continue@loop
                    }
                }
                println()
                return true
            }

            loop@ for (c in 0 until width) {
                for (r in 0 until height) {
                    print(board[r * width + c])
                    if (board[width * r + c] != -1) {
                        println()
                        continue@loop
                    }
                }
                println()
                return true
            }
            return false
        }

        fun getUnmarkedSum(): Int = board.filter { it != -1 }.sum()
    }

    fun part1(input: List<BingoBoard>, numbers: List<Int>): Int {
        numbers.forEach { number ->
            input.forEach { board ->
                if (board.checkNumber(number)) {
                    println("Winning number $number")
                    println(board.board)
                    return number * board.getUnmarkedSum()
                }
            }
        }

        return 0
    }

    fun part2(input: List<BingoBoard>, numbers: List<Int>): Int {
        val minput = input.toMutableList()
        var lastWinBoard: BingoBoard? = null
        var lastWinNumber = 0
        numbers.forEach { number ->
            val miterator = minput.iterator()
            while(miterator.hasNext()) {
                val board = miterator.next()
                if (board.checkNumber(number)) {
                    lastWinBoard = board
                    lastWinNumber = number
                    miterator.remove()
                }
            }
        }
        return lastWinNumber * lastWinBoard!!.getUnmarkedSum()
    }

    fun readBingo(input: List<String>): List<BingoBoard> {
        val bingoBoards = mutableListOf<BingoBoard>()
        var numbers = mutableListOf<Int>()
        input.forEach {
            if (it.isBlank()) {
                bingoBoards.add(BingoBoard(numbers))
                numbers = mutableListOf<Int>()
            } else {
                numbers.addAll(it.trim().split("\\s+".toRegex()).map(String::toInt))
            }
        }
        bingoBoards.add(BingoBoard(numbers))
        return bingoBoards.toList()
    }

    // test if implementation meets criteria from the description, like:
    var testInput = readInput("Day04_test")
    var bingoNumbers = testInput.first().split(',').map(String::toInt)
    testInput = testInput.drop(2)
    var bingoBoards = readBingo(testInput)
    check(part1(bingoBoards, bingoNumbers) == 4512)
    check(part2(bingoBoards, bingoNumbers) == 1924)

    var input = readInput("Day04_input")
    bingoNumbers = input.first().split(',').map(String::toInt)
    input = input.drop(2)
    bingoBoards = readBingo(input)
    println(part1(bingoBoards, bingoNumbers))
    println(part2(bingoBoards, bingoNumbers))
}
