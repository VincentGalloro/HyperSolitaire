package data.moves

import data.game.CardColumn
import data.game.GameCard
import data.game.GameState
import velvet.structs.VColor

class ColumnTransferMove(val fromCardColumn: CardColumn,
                         val toCardColumn: CardColumn,
                         val stackRootCard: GameCard) : Move {

    override fun makeMove(gameState: GameState): GameState? {
        if(fromCardColumn !in gameState.cardColumns ||
            toCardColumn !in gameState.cardColumns ||
            fromCardColumn===toCardColumn){
            return null
        } //Invalid columns for transfer

        //return null if transferFrom stack is illegal or transferTo stack has bad receiver
        val (newFromCardColumn, transferStack) = fromCardColumn.transferStackOut(stackRootCard) ?: return null
        val newToCardColumn = toCardColumn.transferStackIn(transferStack) ?: return null

        val newCardColumns = gameState.cardColumns.map {
            when(it){
                fromCardColumn -> newFromCardColumn
                toCardColumn -> newToCardColumn
                else -> it
            }
        }
        return GameState(newCardColumns, gameState.cardStacks, gameState.deck)
    }

    override fun toString(gameState: GameState): String {
        val fromCardColumnIndex = gameState.cardColumns.indexOf(fromCardColumn)
        val toCardColumnIndex = gameState.cardColumns.indexOf(toCardColumn)
        val stackRootCardIndex = fromCardColumn.gameCards.indexOf(stackRootCard)

        if(fromCardColumnIndex==-1 || toCardColumnIndex==-1 || stackRootCardIndex==-1) return "[Invalid Column]"

        return "Transfer $stackRootCard (stack size ${fromCardColumn.gameCards.size - stackRootCardIndex})" +
                " from column ${fromCardColumnIndex+1} to column ${toCardColumnIndex+1}"
    }
    override val color = if(toCardColumn.gameCards.isEmpty()) VColor(255, 0, 255) else VColor.BLACK
}