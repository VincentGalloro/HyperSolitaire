package data.moves

import data.game.CardColumn
import data.game.CardStack
import data.game.GameState
import velvet.structs.VColor

class PutUpMove(val fromCardColumn: CardColumn,
                val toCardStack: CardStack) : Move {

    override fun makeMove(gameState: GameState): GameState? {
        if(fromCardColumn !in gameState.cardColumns ||
                toCardStack !in gameState.cardStacks) return null

        val (newFromCardColumn, gameCard) = fromCardColumn.transferTopOff() ?: return null
        val newToCardStack = toCardStack.transferCardIn(gameCard) ?: return null

        val newCardColumns = gameState.cardColumns.map { if(it===fromCardColumn) newFromCardColumn else it }
        val newCardStacks = gameState.cardStacks.map { if(it===toCardStack) newToCardStack else it }

        return GameState(newCardColumns, newCardStacks, gameState.deck)
    }

    override fun toString(gameState: GameState): String {
        val fromCardColumnIndex = gameState.cardColumns.indexOf(fromCardColumn)
        val toCardStackIndex = gameState.cardStacks.indexOf(toCardStack)
        val stackRootCard = fromCardColumn.top() ?: return "[Invalid Empty Column]"

        if(fromCardColumnIndex==-1) return "[Invalid Column]"
        if(toCardStackIndex==-1) return "[Invalid Stack]"

        return "Put up $stackRootCard from column ${fromCardColumnIndex+1} to stack ${toCardStackIndex+1}"
    }
    override val color = VColor(0, 255, 0)
}