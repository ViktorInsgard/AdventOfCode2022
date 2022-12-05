package day1

import java.io.File

fun main(args: Array<String>) {
    val input = File(args[0]).readText()
    println(findAnswerA(input))
    println(findAnswerB(input))
}

fun findAnswerA(input: String) : Int {
    return input
        .split("\r\n\r\n")
        .maxBy { s ->
            s.split("\r\n").sumOf { i ->
                i.toInt()
            }
        }
        .split("\r\n")
        .sumOf { i ->
            i.toInt()
        }
}

fun findAnswerB(input: String) : Int {
    return input
        .split("\r\n\r\n")
        .sortedBy { s ->
            s.split("\r\n").sumOf { i ->
                i.toInt()
            }
        }
        .reversed()
        .take(3)
        .sumOf { s ->
            s.split("\r\n").sumOf { i ->
                i.toInt()
            }
        }
}