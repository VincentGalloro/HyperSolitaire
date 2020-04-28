package data

import data.game.Deck
import data.game.GameState
import data.moves.DealMove
import data.moves.Move

class Game private constructor(initialState: GameState) {

    companion object{
        fun createNewGame(): Game{
            val game = Game(GameState.newGameState(Deck.createShuffledDeck()))
            repeat(3){ game.makeMove(DealMove(visible = false)) }
            game.makeMove(DealMove())
            return game
        }
    }

    var currentState = initialState
        set(value) {
            if(value in timeLine){
                field = value
                onStateUpdated?.invoke()
            }
        }

    val timeLine = Timeline(initialState)

    var onStateUpdated: (()->Unit)? = null

    fun makeMove(move: Move){
        val newState = timeLine.createTransition(currentState, move) ?: return
        currentState = newState
    }
}