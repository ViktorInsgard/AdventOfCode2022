package day8

fun main(args: Array<String>) {
    val input = java.io.File(args[0]).readLines()

    val testList = listOf("58885", "47444", "12345", "68666", "58885")
    println("A: " + getAnswerA(input))
    println("B: " + getAnswerB(input))
}

fun getAnswerA(inputLines: List<String>): Int {
    val columns = inputLines[0].length
    val rows = inputLines.size
    val grid = VisibilityGrid(columns, rows)

    getColumns(inputLines).forEachIndexed { columnIndex, column ->
        var maxHeight = -1
        column.forEachIndexed { rowIndex, treeHeight ->
            if (treeHeight > maxHeight) {
                grid.setVisible(columnIndex, rowIndex)
                maxHeight = treeHeight
            }
        }
    }

    getRows(inputLines).forEachIndexed { rowIndex, row ->
        var maxHeight = -1
        row.forEachIndexed { columnIndex, treeHeight ->
            if (treeHeight > maxHeight) {
                grid.setVisible(columnIndex, rowIndex)
                maxHeight = treeHeight
            }
        }
    }

    getColumns(inputLines).forEachIndexed { columnIndex, column ->
        var maxHeight = -1
        column.withIndex().reversed().forEach {
            if (it.value > maxHeight) {
                grid.setVisible(columnIndex, it.index)
                maxHeight = it.value
            }
        }
    }

    getRows(inputLines).forEachIndexed { rowIndex, row ->
        var maxHeight = -1

        row.withIndex().reversed().forEach {
            if (it.value > maxHeight) {
                grid.setVisible(it.index, rowIndex)
                maxHeight = it.value
            }
        }
    }

    return grid.countVisible()
}

fun getAnswerB(inputLines: List<String>): Int {
    val grid = HeightGrid.from(inputLines)

    return (0 until grid.columns).flatMap { column ->
        (0 until grid.rows).map { row ->
            grid.getScenicScoreOf(column, row)
        }
    }.max()
}

fun getColumns(inputLines: List<String>): List<List<Int>> {
    val columns = inputLines[0].length
    return (0 until columns).map { col ->
        inputLines.map { row ->
            row[col].digitToInt()
        }
    }
}

fun getRows(inputLines: List<String>): List<List<Int>> {
    return inputLines.map { row ->
        row.map { char ->
            char.digitToInt()
        }
    }
}

class VisibilityGrid(private val columns: Int, private val rows: Int) {
    private val grid = Array(columns) { Array<Boolean>(rows) { false } }

    fun setVisible(column: Int, row: Int) {
        grid[column][row] = true
    }

    fun countVisible(): Int {
        return grid.flatMap { row ->
            row.asSequence()
        }
            .count {
                it
            }
    }

    override fun toString(): String {
        var string = ""
        grid.forEach { row -> string += row.contentToString() + "\n" }
        return string
    }
}

class HeightGrid private constructor(val columns: Int, val rows: Int) {
    private val grid = Array(columns) { Array(rows) { 0 } }

    companion object {
        fun from(inputLines: List<String>): HeightGrid {
            val grid = HeightGrid(inputLines[0].length, inputLines.size)
            inputLines.forEachIndexed { rowIndex, rowContent ->
                rowContent.forEachIndexed { columnIndex, height ->
                    grid.grid[columnIndex][rowIndex] = height.digitToInt()
                }
            }
            return grid
        }
    }

    private fun getHeightsNorthOf(column: Int, row: Int): List<Int> {
        return grid[column].take(row).reversed()
    }

    private fun getHeightsSouthOf(column: Int, row: Int): List<Int> {
        return grid[column].drop(row + 1)
    }

    private fun getHeightsWestOf(column: Int, row: Int): List<Int> {
        return grid.take(column).map { it[row] }.reversed()
    }

    private fun getHeightsEastOf(column: Int, row: Int): List<Int> {
        return grid.drop(column + 1).map { it[row] }
    }

    fun getScenicScoreOf(column: Int, row: Int): Int {
        val treeHeight = grid[column][row]
        return listOf(
            getHeightsNorthOf(column, row),
            getHeightsSouthOf(column, row),
            getHeightsWestOf(column, row),
            getHeightsEastOf(column, row))
        .map { getScenicScoreFromList(treeHeight, it) }
        .reduce { product, height -> product * height }
    }

    private fun getScenicScoreFromList(treeHeight: Int, otherHeights: List<Int>): Int {
        if (otherHeights.isEmpty()) {
            return 0
        }

        return otherHeights.takeWhileInclusive { it < treeHeight }.count()
    }
}

fun <T> List<T>.takeWhileInclusive(predicate: (T) -> Boolean) = sequence {
    with(iterator()) {
        while (hasNext()) {
            val next = next()
            yield(next)
            if (!predicate(next)) break
        }
    }
}
