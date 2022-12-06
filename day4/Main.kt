package day4

import java.io.File

fun main(args: Array<String>) {
    val input = File(args[0]).readLines()
    println("A: " + getAnswerA(input))
    println("B: " + getAnswerB(input))
}

fun getAnswerA(inputLines: List<String>) : Int {
    return inputLines.count { line -> firstContainsLast(line) || lastContainsFirst(line) }
}

fun firstContainsLast(line: String) : Boolean {
    val elements = line.split(",", "-").map { s -> s.toInt() }
    return (elements[0] <= elements[2]) && (elements[1] >= elements[3])
}

fun lastContainsFirst(line: String) : Boolean {
    val elements = line.split(",", "-").map { s -> s.toInt() }
    return (elements[0] >= elements[2]) && (elements[1] <= elements[3])
}

fun getAnswerB(inputLines: List<String>) : Int {
    return inputLines.count { line ->
        val elements = line.split(",", "-").map { s -> s.toInt() }
        val first = IntRange(elements[0], elements[1])
        val second = IntRange(elements[2], elements[3])
        first.intersect(second).isNotEmpty()
    }
}