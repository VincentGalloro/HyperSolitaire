package data.moves

import data.game.GameCard
import data.game.GameState
import velvet.structs.VColor

class DealMove(private val visible: Boolean = true): Move {

    override fun makeMove(gameState: GameState): GameState? {
        val (newDeck, dealtCards) = gameState.deck.deal(gameState.cardColumns.size)
        if(dealtCards.size < gameState.cardColumns.size) return null //Not enough cards left to deal row

        val newColumns = gameState.cardColumns.zip(dealtCards.map { GameCard(it, visible) })
            .map { (column, gameCard) -> column.addCard(gameCard) }
        return GameState(newColumns, gameState.cardStacks, newDeck)
    }

    override fun toString(gameState: GameState): String {
        return "Deal a row"
    }
    override val color = VColor(0, 180, 255)
}