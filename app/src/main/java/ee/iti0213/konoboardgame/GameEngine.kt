package ee.iti0213.konoboardgame

import kotlin.collections.ArrayList

object GameEngine {

    private val gameBoard = Array(5) { Array(5) { Color.CLEAR } }
    private var gameState: GameState? = null
    private var blueScore = 0
    private var orangeScore = 0

    fun getGameBoard(): Array<Array<Color>> {
        return this.gameBoard
    }

    fun getGameState(): GameState? {
        return this.gameState
    }

    fun getBlueScore(): Int {
        return this.blueScore
    }

    fun getOrangeScore(): Int {
        return this.orangeScore
    }

    fun resetGame() {
        gameState = if (Preferences.isBlueStarts()) GameState.BLUE_TURN else GameState.ORANGE_TURN
        resetBoard()
    }

    fun isAITurn(): Boolean {
        return (gameState == GameState.BLUE_TURN && Preferences.isBlueAI()
                || gameState == GameState.ORANGE_TURN && Preferences.isOrangeAI())
    }

    fun doAIMove(): String? {
        val color = if (gameState == GameState.BLUE_TURN) Color.BLUE else Color.ORANGE
        val colorH = if (gameState == GameState.BLUE_TURN) Color.BLUE_H else Color.ORANGE_H
        var chosenSquare: Array<Int>? = null
        var chosenMove: Array<Int>? = null

        val winningMove = findWinningMove(color)
        if (winningMove.size > 0) {
            chosenSquare = winningMove.first()
            chosenMove = winningMove.last()
        }

        if (chosenSquare == null) {
            val coloredSquares = findAllSquaresByColor(color, gameBoard)
            coloredSquares.shuffle()
            outerLoop@
            for (square in coloredSquares) {
                for (move in findDiagonalsWithColor(square.first(), square.last(), Color.CLEAR)) {
                    chosenSquare = square
                    chosenMove = move
                    if (isMoveAggressive(color, square, move)) break@outerLoop
                }
            }
        }

        registerMove(chosenSquare!!.first(), chosenSquare.last(), color, colorH)
        registerMove(chosenMove!!.first(), chosenMove.last(), color, colorH)

        return gameOverIfWinner()
    }

    fun doPlayerMove(buttonTag: String): String? {
        if (isAITurn()) return null

        val y = Character.getNumericValue(buttonTag.first())
        val x = Character.getNumericValue(buttonTag.last())
        when (gameState) {
            GameState.BLUE_TURN -> registerMove(y, x, Color.BLUE, Color.BLUE_H)
            GameState.ORANGE_TURN -> registerMove(y, x, Color.ORANGE, Color.ORANGE_H)
            else -> return null
        }

        return gameOverIfWinner()
    }

    private fun registerMove(y: Int, x: Int, color: Color, colorH: Color) {
        when (gameBoard[y][x]) {
            color -> {
                clearBoardHighlights()
                gameBoard[y][x] = colorH
            }
            colorH -> gameBoard[y][x] = color
            Color.CLEAR -> {
                val movable = findDiagonalsWithColor(y, x, colorH)
                if (movable.size > 0) {
                    gameBoard[movable.first().first()][movable.first().last()] = Color.CLEAR
                    gameBoard[y][x] = color
                    clearBoardHighlights()
                    gameState =
                        if (gameState == GameState.BLUE_TURN) GameState.ORANGE_TURN
                        else GameState.BLUE_TURN
                }
            }
            else -> return
        }
    }

    private fun clearBoardHighlights() {
        for (y in gameBoard.indices) {
            for (x in gameBoard[y].indices) {
                if (gameBoard[y][x] == Color.BLUE_H) {
                    gameBoard[y][x] = Color.BLUE
                } else if (gameBoard[y][x] == Color.ORANGE_H) {
                    gameBoard[y][x] = Color.ORANGE
                }
            }
        }
    }

    private fun findDiagonalsWithColor(y: Int, x: Int, color: Color): ArrayList<Array<Int>> {
        val result = arrayListOf<Array<Int>>()
        for (i in -1..1 step 2) {
            for (j in -1..1 step 2) {
                if (y + i in 0..4 && x + j in 0..4) {
                    if (gameBoard[y + i][x + j] == color) {
                        result.add(arrayOf(y + i, x + j))
                    }
                }
            }
        }
        return result
    }

    private fun gameOverIfWinner(): String? {
        if (checkForWinningPosition(Color.BLUE, 4, 3, gameBoard)) {
            gameState = GameState.BLUE_WIN
            blueScore++
            return GameState.BLUE_WIN.msg
        } else if (checkForWinningPosition(Color.ORANGE, 0, 1, gameBoard)) {
            gameState = GameState.ORANGE_WIN
            orangeScore++
            return GameState.ORANGE_WIN.msg
        } else if (!isAbleToMove(Color.BLUE)) {
            gameState = GameState.ORANGE_WIN
            orangeScore++
            return GameState.ORANGE_WIN.msg
        } else if (!isAbleToMove(Color.ORANGE)) {
            gameState = GameState.BLUE_WIN
            blueScore++
            return GameState.BLUE_WIN.msg
        }
        return null
    }

    private fun checkForWinningPosition(color: Color, y1: Int, y2: Int, gameBoard: Array<Array<Color>>): Boolean {
        val checkableSquares = findAllSquaresByColor(color, gameBoard)
        for (cs in checkableSquares) {
            if (cs.first() == y1) continue
            if (cs.first() == y2 && (cs.last() == 0 || cs.last() == 4)) continue
            return false
        }
        return true
    }

    private fun isAbleToMove(color: Color): Boolean {
        val checkableSquares = findAllSquaresByColor(color, gameBoard)
        for (cs in checkableSquares) {
            if (findDiagonalsWithColor(cs.first(), cs.last(), Color.CLEAR).size > 0)
                return true
        }
        return false
    }

    private fun findAllSquaresByColor(color: Color, gameBoard: Array<Array<Color>>): ArrayList<Array<Int>> {
        val result = arrayListOf<Array<Int>>()
        val colorH = if (color == Color.BLUE) Color.BLUE_H else Color.ORANGE_H
        for (y in gameBoard.indices) {
            for (x in gameBoard[y].indices) {
                if (gameBoard[y][x] == color || gameBoard[y][x] == colorH) {
                    result.add(arrayOf(y, x))
                }
            }
        }
        return result
    }

    private fun findWinningMove(color: Color): ArrayList<Array<Int>> {
        val result = arrayListOf<Array<Int>>()
        val y1 = if (color == Color.BLUE) 4 else 0
        val y2 = if (color == Color.BLUE) 3 else 1
        val tempBoard = gameBoard.deepCopy()
        for (square in findAllSquaresByColor(color, gameBoard)) {
            for (move in findDiagonalsWithColor(square.first(), square.last(), Color.CLEAR)) {
                tempBoard[square.first()][square.last()] = Color.CLEAR
                tempBoard[move.first()][move.last()] = color
                if (checkForWinningPosition(color, y1, y2, tempBoard)) {
                    result.add(arrayOf(square.first(), square.last()))
                    result.add(arrayOf(move.first(), move.last()))
                    return result
                }
                tempBoard[square.first()][square.last()] = color
                tempBoard[move.first()][move.last()] = Color.CLEAR
            }
        }
        return result
    }

    private fun isMoveAggressive(color: Color, square: Array<Int>, move: Array<Int>): Boolean {
        if (color == Color.BLUE) {
            if (square.first() < move.first()) return true
        } else if (square.first() > move.first()) return true
        return false
    }

    private fun Array<Array<Color>>.deepCopy() = Array(size) { get(it).clone() }

    private fun resetBoard() {
        for (y in gameBoard.indices) {
            for (x in gameBoard[y].indices) {
                if (y == 0) gameBoard[y][x] = Color.BLUE
                else if (y == 4) gameBoard[y][x] = Color.ORANGE
                else if (y == 1 && (x == 0 || x == 4)) gameBoard[y][x] = Color.BLUE
                else if (y == 3 && (x == 0 || x == 4)) gameBoard[y][x] = Color.ORANGE
                else gameBoard[y][x] = Color.CLEAR
            }
        }
    }
}