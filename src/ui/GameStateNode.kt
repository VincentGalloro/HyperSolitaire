package ui

import data.MoveGenerator
import data.game.CardColumn
import data.game.CardStack
import data.game.GameState
import data.moves.Move
import velvet.structs.Bounds
import velvet.structs.VColor
import velvet.structs.Vector
import velvet.ui.UINode
import velvet.ui.premade.components.BasicComponent
import velvet.ui.premade.nodes.OutlinedTextNode
import velvet.ui.vcontainer.VContainer
import velvet.ui.vcontainer.velements.TextElement

class GameStateNode {

    private val moveGenerator = MoveGenerator()

    val uiNode = UINode()

    var cardContainerNodes: List<CardContainerNode> = emptyList()
    var selected: CardNode? = null

    var onCardToCardTransfer: ((CardNode, CardNode) -> Unit)? = null
    var onCardToColumnTransfer: ((CardNode, CardColumn) -> Unit)? = null
    var onCardToStackTransfer: ((CardNode, CardStack) -> Unit)? = null
    var onMoveRequest: ((Move) -> Unit)? = null

    private fun onCardSelect(cardNode: CardNode){
        selected?.squareElement?.outlineColor = VColor.BLACK
        selected = (selected?.let {
            onCardToCardTransfer?.invoke(it, cardNode)
            val getter: ()->CardNode? = { null }
            getter
        } ?: { cardNode })()
        selected?.squareElement?.outlineColor = VColor(255, 255, 0)
    }

    private fun onCardColumnSelect(cardColumn: CardColumn){
        selected?.let {
            it.squareElement.outlineColor = VColor.BLACK
            onCardToColumnTransfer?.invoke(it, cardColumn)
        }
        selected = null
    }

    private fun onCardStackSelect(cardStack: CardStack){
        selected?.let {
            it.squareElement.outlineColor = VColor.BLACK
            onCardToStackTransfer?.invoke(it, cardStack)
        }
        selected = null
    }

    fun loadGameState(gameState: GameState){
        uiNode.subNodes.clear()
        uiNode.vContainers.clear()

        cardContainerNodes = gameState.cardColumns.mapIndexed { index, column ->
            CardContainerNode(0.37).also {
                it.uiNode.bounds = Bounds.fromStartOfSize(Vector(20 + index*130, 180), Vector(120, 600))
                it.loadCards(column.gameCards)
                it.cardNodes.forEach { cardNode ->
                    cardNode.uiNode.uiComponents.add(object : BasicComponent(){
                        init{
                            uiEventListener.onMousePress = { onCardSelect(cardNode) }
                        }
                    })
                }
                it.uiNode.uiComponents.add(object : BasicComponent(){
                    init{
                        uiEventListener.onMousePress = { onCardColumnSelect(column) }
                    }
                })
            }
        } + gameState.cardStacks.mapIndexed { index, stack ->
            CardContainerNode(0.0).also {
                it.uiNode.bounds = Bounds.fromStartOfSize(Vector(20 + index*130, 20), Vector(120, 150))
                    .setWidth(100.0, 0.5)
                it.loadCards(stack.gameCards)
                it.cardNodes.forEach { cardNode ->
                    cardNode.uiNode.uiComponents.add(object : BasicComponent(){
                        init{
                            uiEventListener.onMousePress = { onCardSelect(cardNode) }
                        }
                    })
                }
                it.uiNode.uiComponents.add(object : BasicComponent(){
                    init{
                        uiEventListener.onMousePress = { onCardStackSelect(stack) }
                    }
                })
            }
        }
        val moveContainers = moveGenerator.generate(gameState).mapIndexed { index, move ->
            OutlinedTextNode(move.toString(gameState), Vector(0.95)).also {
                it.uiNode.bounds =
                    Bounds.fromStartOfSize(Vector(1200.0, 20.0 + index*70.0), Vector(550.0, 60.0))
                it.uiNode.uiComponents.add(object : BasicComponent(){
                    init{
                        uiEventListener.onMousePress = { onMoveRequest?.invoke(move) }
                    }
                })
            }
        }

        uiNode.subNodes += cardContainerNodes.map { it.uiNode } + moveContainers.map { it.uiNode }
    }
}