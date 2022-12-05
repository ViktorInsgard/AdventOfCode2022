package day2

import java.io.File

fun main(args: Array<String>) {
    val input = File(args[0]).readLines()
    println("A: " + input.sumOf { round -> calculatePointsA(round) })
    println("B: " + input.sumOf { round -> calculatePointsB(round) })
}

const val rock = 1
const val paper = 2
const val scissors = 3
const val loss = 0
const val draw = 3
const val win = 6

fun calculatePointsA(round: String) : Int {
    when (round) {
        "A X" -> return rock + draw
        "A Y" -> return paper + win
        "A Z" -> return scissors + loss
        "B X" -> return rock + loss
        "B Y" -> return paper + draw
        "B Z" -> return scissors + win
        "C X" -> return rock + win
        "C Y" -> return paper + loss
        "C Z" -> return scissors + draw
    }
    throw java.lang.IllegalArgumentException("Unknown round: $round")
}

fun calculatePointsB(round: String) : Int {
    when (round) {
        "A X" -> return loss + scissors
        "A Y" -> return draw + rock
        "A Z" -> return win + paper
        "B X" -> return loss + rock
        "B Y" -> return draw + paper
        "B Z" -> return win + scissors
        "C X" -> return loss + paper
        "C Y" -> return draw + scissors
        "C Z" -> return win + rock
    }
    throw java.lang.IllegalArgumentException("Unknown round: $round")
}
