enum class PacketType(val id: Long) {
    Literal(4L), Sum(0L), Product(1L), Minimum(2L), Maximum(3L),
    GreaterThan(5L), LessThan(6L), EqualTo(7L);

    companion object {
        fun fromId(id: Long): PacketType {
            return requireNotNull(PacketType.values().find { it.id == id }) { "Invalid packet type" }
        }
    }

    fun operatorString(): String = when (id) {
        0L -> "+"
        1L -> "*"
        2L -> "min"
        3L -> "max"
        5L -> ">"
        6L -> "<"
        7L -> "=="
        else -> TODO()

    }
}

sealed class Packet(val version: Long, val type: PacketType) {
    class LiteralPacket(version: Long, type: PacketType, private val value: Long) : Packet(version, type) {
        override fun toString(): String {
            return "Literal ($value)"
        }

        override fun versionSum(): Long {
            return version
        }

        override fun getValue(): Long {
            return value
        }
    }

    class OperatorPacket(version: Long, type: PacketType, val innerPackets: List<Packet>) : Packet(version, type) {
        override fun toString(): String {
            return "Operator ${type.operatorString()} $innerPackets"
        }

        override fun versionSum(): Long {
            return version + innerPackets.sumOf { it.versionSum() }
        }

        override fun getValue(): Long {
            return when (type) {
                PacketType.Literal -> TODO()
                PacketType.Sum -> innerPackets.sumOf { it.getValue() }
                PacketType.Product -> innerPackets.fold(1) { acc, p -> acc * p.getValue() }
                PacketType.Minimum -> innerPackets.minOf { it.getValue() }
                PacketType.Maximum -> innerPackets.maxOf { it.getValue() }
                PacketType.GreaterThan -> {
                    val (f, s) = innerPackets
                    if (f.getValue() > s.getValue()) 1 else 0
                }
                PacketType.LessThan -> {
                    val (f, s) = innerPackets
                    if (f.getValue() < s.getValue()) 1 else 0
                }
                PacketType.EqualTo -> {
                    val (f, s) = innerPackets
                    if (f.getValue() == s.getValue()) 1 else 0
                }
            }
        }


    }

    abstract fun versionSum(): Long

    abstract fun getValue(): Long
}

fun main() {

    val hexToBinMapping = mapOf(
        '0' to "0000",
        '1' to "0001",
        '2' to "0010",
        '3' to "0011",
        '4' to "0100",
        '5' to "0101",
        '6' to "0110",
        '7' to "0111",
        '8' to "1000",
        '9' to "1001",
        'A' to "1010",
        'B' to "1011",
        'C' to "1100",
        'D' to "1101",
        'E' to "1110",
        'F' to "1111"
    )

    fun String.toBinaryRepresentation(): String = buildString {
        this@toBinaryRepresentation.forEach { append(hexToBinMapping[it]) }
    }

    fun decodeLiteral(encodedLiteral: MutableList<Char>): Long {
        var stop = false
        var bitsReaded = 0
        return buildString {
            while (!stop) {
                val bits = encodedLiteral.subList(0, 5)
                stop = bits.removeFirst() == '0'
                bitsReaded += 5
                append(bits.joinToString(""))
                bits.clear()
            }
        }.toLong(2)
    }

    fun decode(encodedString: MutableList<Char>, readLimit: Int = -1): List<Packet> {
        val packets = mutableListOf<Packet>()
        var packetsAdded = 0
        while (encodedString.isNotEmpty() && (readLimit == -1 || packetsAdded != readLimit)) {
            if (encodedString.all { it == '0' }) {
                println("Remaining bits are all zero, assuming end of packet")
                break
            }
            val packetVersionSublist = encodedString.subList(0, 3)
            val packetVersion = packetVersionSublist.joinToString("").toLong(2)
            packetVersionSublist.clear()

            val packetTypeIdSublist = encodedString.subList(0, 3)
            val packetTypeId = PacketType.fromId(packetTypeIdSublist.joinToString("").toLong(2))
            packetTypeIdSublist.clear()

            when (packetTypeId) {
                PacketType.Literal -> {
                    packets.add(
                        Packet.LiteralPacket(
                            version = packetVersion,
                            type = packetTypeId,
                            value = decodeLiteral(encodedString)
                        )
                    )
                    packetsAdded++
                }
                else -> {
                    val operatorMode = encodedString.removeFirst()
                    if (operatorMode == '0') {
                        // Total length of packet
                        val packetLengthBits = encodedString.subList(0, 15)
                        val packetLength = packetLengthBits.joinToString("").toInt(2)
                        packetLengthBits.clear()

                        val innerPacketEncoded = encodedString.subList(0, packetLength)
                        packets.add(
                            Packet.OperatorPacket(
                                packetVersion,
                                packetTypeId,
                                decode(innerPacketEncoded.toMutableList())
                            )
                        )
                        packetsAdded++
                        innerPacketEncoded.clear()

                    } else {
                        // Number of SubPackets
                        val subPacketsCountBits = encodedString.subList(0, 11)
                        val subPacketsCount = subPacketsCountBits.joinToString("").toInt(2)
                        subPacketsCountBits.clear()

                        packets.add(
                            Packet.OperatorPacket(
                                version = packetVersion,
                                type = packetTypeId,
                                innerPackets = decode(encodedString, subPacketsCount)
                            )
                        )
                        packetsAdded++
                    }
                }
            }

        }

        return packets
    }

    fun part1(input: String): Int {
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

//    println(decode("C200B40A82".toBinaryRepresentation().toMutableList()).also {
//        println("Sum is ${it.sumOf { p -> p.versionSum() }}")
//        println("Value is ${it.first().getValue()}")
//    })
//    println()
//    println(decode("04005AC33890".toBinaryRepresentation().toMutableList()).also {
//        println("Sum is ${it.sumOf { p -> p.versionSum() }}")
//        println("Value is ${it.first().getValue()}")
//    })
//    println()
//    println(decode("880086C3E88112".toBinaryRepresentation().toMutableList()).also {
//        println("Sum is ${it.sumOf { p -> p.versionSum() }}")
//        println("Value is ${it.first().getValue()}")
//    })
//    println()
//    println(decode("CE00C43D881120".toBinaryRepresentation().toMutableList()).also {
//        println("Sum is ${it.sumOf { p -> p.versionSum() }}")
//    })
//    println()
//    println(decode("D8005AC2A8F0".toBinaryRepresentation().toMutableList()).also {
//        println("Sum is ${it.sumOf { p -> p.versionSum() }}")
//        println("Value is ${it.first().getValue()}")
//    })
//    println()
//    println(decode("F600BC2D8F".toBinaryRepresentation().toMutableList()).also {
//        println("Sum is ${it.sumOf { p -> p.versionSum() }}")
//        println("Value is ${it.first().getValue()}")
//    })
//    println()
//    println(decode("9C005AC2F8F0".toBinaryRepresentation().toMutableList()).also {
//        println("Sum is ${it.sumOf { p -> p.versionSum() }}")
//        println("Value is ${it.first().getValue()}")
//    })
//    println()
    println(decode("9C0141080250320F1802104A08".toBinaryRepresentation().toMutableList()).also {
        println("Sum is ${it.sumOf { p -> p.versionSum() }}")
        println("Value is ${it.first().getValue()}")
    })
    println()

    println(decode(readInput("Day16_input").first().toBinaryRepresentation().toMutableList()).also {
        println("Sum is ${it.sumOf { p -> p.versionSum() }}")
        println("Value is ${it.first().getValue()}")
    })
}
