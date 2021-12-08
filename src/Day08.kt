fun main() {

    data class Data(
        val test: List<String>,
        val output: List<String>
    )

    val normalDigitSegments = mapOf<String, Int>(
        "abcefg" to 0,
        "cf" to 1,
        "acdeg" to 2,
        "acdfg" to 3,
        "bcdf" to 4,
        "abdfg" to 5,
        "abdefg" to 6,
        "acf" to 7,
        "abcdefg" to 8,
        "abcdfg" to 9
    )

    fun part1(input: List<Data>): Int {
        return input.fold(0) { acc, data -> acc + data.output.count { digit -> digit.length in listOf(2, 4, 3, 7) } }
    }

    fun String.diff(other: String): Set<Char> {
        val diff = mutableSetOf<Char>()
        forEach {
            if (!other.contains(it)) diff.add(it)
        }
        other.forEach {
            if (!contains(it)) diff.add(it)
        }
        return diff.toSet()
    }

    fun String.sorted(): String  = toCharArray().sorted().joinToString("")

    fun createMapping(testDigits: List<String>): MutableMap<String, Int> {
        val mapping = mutableMapOf<String, Int>()
        val encodingOfOne = testDigits.find { it.length == 2 }!!
        val encodingOfFour = testDigits.find { it.length == 4 }!!
        val encodingOfSeven = testDigits.find { it.length == 3 }!!
        val encodingOfEight = testDigits.find { it.length == 7 }!!
        mapping[encodingOfOne.sorted()] = 1
        mapping[encodingOfFour.sorted()] = 4
        mapping[encodingOfSeven.sorted()] = 7
        mapping[encodingOfEight.sorted()] = 8

        val aSignal = encodingOfSeven.find { it !in encodingOfOne }
        val bdSignal = encodingOfFour.toCharArray().subtract(encodingOfOne.toCharArray().toSet())
        val egSignal =
            encodingOfEight.toCharArray().subtract(encodingOfSeven.toCharArray().toSet()).subtract(bdSignal)

        val twoThreeFive = testDigits.filter { it.length == 5 }
        val encodingOfThree: String
        val encodingOfTwo: String
        val encodingOfFive: String
        if (twoThreeFive[0].diff(twoThreeFive[1]).size == 4) {
            encodingOfThree = twoThreeFive[2]
            when {
                egSignal.all { c -> c in twoThreeFive[0] } -> {
                    encodingOfTwo = twoThreeFive[0]
                    encodingOfFive = twoThreeFive[1]
                }
                else -> {
                    encodingOfTwo = twoThreeFive[1]
                    encodingOfFive = twoThreeFive[0]
                }
            }
        } else if (twoThreeFive[1].diff(twoThreeFive[2]).size == 4) {
            encodingOfThree = twoThreeFive[0]
            when {
                egSignal.all { c -> c in twoThreeFive[1] } -> {
                    encodingOfTwo = twoThreeFive[1]
                    encodingOfFive = twoThreeFive[2]
                }
                else -> {
                    encodingOfTwo = twoThreeFive[2]
                    encodingOfFive = twoThreeFive[1]
                }
            }
        } else {
            encodingOfThree = twoThreeFive[1]
            when {
                egSignal.all { c -> c in twoThreeFive[0] } -> {
                    encodingOfTwo = twoThreeFive[0]
                    encodingOfFive = twoThreeFive[2]
                }
                else -> {
                    encodingOfTwo = twoThreeFive[2]
                    encodingOfFive = twoThreeFive[0]
                }
            }
        }
        mapping[encodingOfFive.sorted()] = 5
        mapping[encodingOfTwo.sorted()] = 2
        mapping[encodingOfThree.sorted()] = 3

        val zeroSixNine = testDigits.filter { it.length == 6 }
        val encodingOfSix = zeroSixNine.find { it.toCharArray().subtract(encodingOfOne.toSet()).size == 5 }!!
        val encodingOfNine = zeroSixNine.find { it.toCharArray().subtract(encodingOfThree.toSet()).size == 1 }!!
        val encodingOfZero = zeroSixNine.subtract(setOf(encodingOfSix, encodingOfNine)).first()

        mapping[encodingOfSix.sorted()] = 6
        mapping[encodingOfNine.sorted()] = 9
        mapping[encodingOfZero.sorted()] = 0

        return mapping
    }

    fun part2(input: List<Data>): Int {
        var count = 0
        input.forEach { data ->
            val mapping = createMapping(data.test)
            count += buildString {
                data.output.forEach {
                    append(mapping[it.sorted()])
                }
            }.toInt()
        }
        return count
    }

    fun String.toData(): Data {
        val splitted = split(" | ")
        return Data(
            test = splitted[0].split(" "),
            output = splitted[1].split(" ")
        )
    }

    fun List<String>.toData() = map { it.toData() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    println(part1(testInput.toData()))
    createMapping("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf".toData().test)
    check(part1(testInput.toData()) == 26)
    check(part2(testInput.toData()) == 61229)

    val input = readInput("Day08_input")
    println(part1(input.toData()))
    println(part2(input.toData()))
}
