package data

import data.game.GameState
import data.moves.ColumnTransferMove
import data.moves.DealMove
import data.moves.Move
import data.moves.PutUpMove

class MoveGenerator {

    fun generate(gameState: GameState): List<Move>{
        return listOfNotNull(DealMove().takeIf {
            it.makeMove(gameState) != null
        }) + gameState.cardColumns.flatMap sourceMap@{ sourceColumn ->
            gameState.cardColumns.flatMap targetMap@{ targetColumn ->
                if(sourceColumn === targetColumn) return@targetMap emptyList<ColumnTransferMove>()

                sourceColumn.gameCards.map { gameCard ->
                    ColumnTransferMove(sourceColumn, targetColumn, gameCard).takeIf {
                        it.makeMove(gameState) != null
                    }
                }
            }.filterNotNull()
        } + gameState.cardColumns.mapNotNull { sourceColumn ->
            gameState.cardStacks.mapNotNull { targetStack ->
                PutUpMove(sourceColumn, targetStack).takeIf {
                    it.makeMove(gameState) != null
                }
            }.firstOrNull()
        }
    }
}