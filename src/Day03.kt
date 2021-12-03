import java.lang.IllegalArgumentException

fun main() {

    fun List<String>.asDecimals(): List<Int> = map {
        it.toInt(2)
    }

    fun String.asDecimal(): Int = toInt(2)

    fun getArrayOfCountOnes(input: List<String>): IntArray {
        val oneBitArray = IntArray(input[0].length)
        input.forEach {
            it.forEachIndexed { index, c ->
                if (c == '1') {
                    oneBitArray[index] = oneBitArray[index] + 1
                }
            }
        }
        return oneBitArray
    }

    fun part1(input: List<String>): Int {
        val oneBitArray = getArrayOfCountOnes(input)
        val listSize = input.size
        val gammaRateString = buildString {
            oneBitArray.forEach {
                append(
                    if (it > (listSize / 2)) {
                        "1"
                    } else {
                        "0"
                    }
                )
            }
        }
//        println(gammaRateString)
        val epsilonRateString = buildString {
            gammaRateString.forEach {
                append(if (it == '1') '0' else '1')
            }
        }
        val gammaRate = gammaRateString.asDecimal()
//        println(gammaRate)
        val epsilonRate = epsilonRateString.asDecimal()
//        println(epsilonRate)
        return gammaRate * epsilonRate
    }

    fun search(input: List<String>, charToFilter: (isOnesMoreCommon: Boolean) -> Char): Int {
        var countOnes = getArrayOfCountOnes(input)
        var toFilter = input
        var currentIndex = 0
        var listSize = toFilter.size
        println(toFilter)
        while (listSize != 1) {
            toFilter = toFilter.filter {
//                println("${countOnes[currentIndex]} / ${(listSize.toFloat() / 2F)}")
                val lookingForBit = charToFilter(countOnes[currentIndex] >= (listSize.toFloat() / 2F))
                it[currentIndex] == lookingForBit
            }
            println(toFilter)
            currentIndex++
            countOnes = getArrayOfCountOnes(toFilter)
            listSize = toFilter.size
        }
        return toFilter.first().asDecimal()
    }

    fun part2(input: List<String>): Int {
        val oxygenGeneratorRating = search(input) { if (it) '1' else '0' }.also { println(it) }
        val co2scrubberRating = search(input) { if (it) '0' else '1' }.also { println(it) }
        return oxygenGeneratorRating * co2scrubberRating
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03_input")
    println(part1(input))
    println(part2(input))
}
