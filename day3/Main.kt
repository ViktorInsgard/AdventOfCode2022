package day3

import java.io.File

fun main(args: Array<String>) {
    val input = File(args[0]).readLines()
    println("A: " + getAnswerA(input))
    println("B: " + getAnswerB(input))
}

fun getAnswerA(inputLines: List<String>) : Int {
    return inputLines.sumOf { line ->
        val firstHalf = line.take(line.length / 2).toSet()
        val lastHalf = line.drop(line.length / 2).toSet()
        firstHalf.intersect(lastHalf).first().getPriority()
    }
}

fun getAnswerB(inputLines: List<String>) : Int {
    return inputLines.chunked(3).sumOf { triplet ->
        val first = triplet[0].toSet()
        val second = triplet[1].toSet()
        val third = triplet[2].toSet()
        first.intersect(second).intersect(third).first().getPriority()
    }
}

fun Char.getPriority() : Int {
    if (!this.isLetter()) {
        throw java.lang.IllegalArgumentException("getPriority is only valid for letters (got $this)")
    }

    return if (this.isLowerCase()) {
        this.code - 96
    } else {
        this.code - 38
    }
}
