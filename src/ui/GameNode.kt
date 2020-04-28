package ui

import data.Game
import data.moves.ColumnTransferMove
import data.moves.DealMove
import data.moves.PutUpMove
import velvet.structs.Bounds
import velvet.structs.Vector
import velvet.ui.UINode
import velvet.ui.premade.components.BasicComponent
import java.awt.event.KeyEvent

class GameNode(game: Game) {

    val uiNode = UINode()

    val gameStateNode = GameStateNode()
    val timelineNode = TimelineNode()

    init{
        gameStateNode.uiNode.bounds = Bounds.fromStartOfSize(Vector(), Vector(1800, 1000))

        gameStateNode.onCardToCardTransfer = { stackRootCard, receiverCard ->
            val srcCol = game.currentState.cardColumns.find { it.gameCards.any { it===stackRootCard.gameCard } }
            val srcStk = game.currentState.cardStacks.find { it.gameCards.any { it===stackRootCard.gameCard } }
            val recCol = game.currentState.cardColumns.find { it.gameCards.any { it===receiverCard.gameCard } }
            val recStk = game.currentState.cardStacks.find { it.gameCards.any { it===receiverCard.gameCard } }

            if(srcCol != null){
                if(recCol != null){
                    game.makeMove(ColumnTransferMove(srcCol, recCol, stackRootCard.gameCard))
                }
                else if(recStk != null){
                    game.makeMove(PutUpMove(srcCol, recStk))
                }
            }
            else if(srcStk != null){
                //cannot handle yet
            }
        }
        gameStateNode.onCardToStackTransfer = { stackRootCard, receiverStack ->
            game.currentState.cardColumns.find {  it.gameCards.any { it===stackRootCard.gameCard } }?.let { srcCol ->
                game.makeMove(PutUpMove(srcCol, receiverStack))
            }
        }
        gameStateNode.onCardToColumnTransfer = { stackRootCard, receiverCol ->
            game.currentState.cardColumns.find { it.gameCards.any { it===stackRootCard.gameCard } }?.let { srcCol ->
                game.makeMove(ColumnTransferMove(srcCol, receiverCol, stackRootCard.gameCard))
            }
        }
        gameStateNode.onMoveRequest = { game.makeMove(it) }

        gameStateNode.uiNode.uiComponents.add(object : BasicComponent(){
            init{
                uiEventListener.onKeyPressed = {
                    if(it == KeyEvent.VK_SPACE){
                        game.makeMove(DealMove())
                    }
                }
            }
        })

        timelineNode.uiNode.bounds = Bounds.fromStartOfSize(Vector(20, 800), Vector(1700, 180))

        timelineNode.onStateSelected = { game.currentState = it }

        val onStateUpdated = {
            gameStateNode.loadGameState(game.currentState)
            timelineNode.loadTimeline(game.timeLine, game.currentState)
        }
        game.onStateUpdated = onStateUpdated
        onStateUpdated()

        uiNode.subNodes.add(gameStateNode.uiNode)
        uiNode.subNodes.add(timelineNode.uiNode)
    }
}