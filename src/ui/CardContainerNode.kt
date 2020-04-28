package ui

import data.game.GameCard
import velvet.main.VGraphics
import velvet.smooth.actuators.impl.BoundsSwishActuator
import velvet.structs.VColor
import velvet.ui.BoundsRestorationMap
import velvet.ui.UINode
import velvet.ui.premade.components.BasicComponent
import velvet.ui.premade.layouts.Layout
import velvet.ui.premade.nodes.ListNode
import velvet.ui.vcontainer.VContainer
import velvet.ui.vcontainer.velements.SquareElement
import java.lang.Integer.max
import kotlin.math.min
import kotlin.math.sqrt

class CardContainerNode(val deafultOffset: Double) {

    val listNode = ListNode(Layout.relativeColumnLayout(1.5, deafultOffset-1.5))
    val uiNode: UINode
        get() = listNode.uiNode

    var cardNodes: List<CardNode> = emptyList()
    var hovered: CardNode? = null

    init{
        uiNode.addContainer(VContainer(SquareElement(VColor.WHITE))){ uiNode.bounds }
        uiNode.uiComponents.add(object : BasicComponent(){
            override fun postRender(uiNode: UINode, g: VGraphics) {
                hovered?.uiNode?.render(g)
            }
        })
    }

    fun loadCards(gameCards: List<GameCard>){
        hovered = null

        val offset = min(deafultOffset, (uiNode.bounds.size.y-uiNode.bounds.size.x*1.5)/(uiNode.bounds.size.x*gameCards.size))
        listNode.layout = Layout.relativeColumnLayout(1.5, offset-1.5)

        cardNodes = gameCards.map {
            CardNode(it).also { cardNode ->
                cardNode.uiNode.uiComponents.add(object : BasicComponent(){
                    init{
                        uiEventListener.onHoverStart = { hovered = cardNode }
                        uiEventListener.onHoverEnd = { hovered = null }
                    }
                })
            }
        }

        listNode.loadNodes(cardNodes.map { it.uiNode })
    }
}