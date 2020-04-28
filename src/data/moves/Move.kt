package data.moves

import data.game.GameState
import velvet.structs.VColor

interface Move {

    fun makeMove(gameState: GameState): GameState?

    fun toString(gameState: GameState): String
    val color: VColor
}