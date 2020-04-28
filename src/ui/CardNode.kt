package ui

import Res
import data.game.GameCard
import velvet.smooth.actuators.impl.BoundsSwishActuator
import velvet.ui.UINode
import velvet.ui.vcontainer.VContainer
import velvet.ui.vcontainer.velements.SpriteElement
import velvet.ui.vcontainer.velements.SquareElement

class CardNode(val gameCard: GameCard) {

    val uiNode = UINode()

    val squareElement = SquareElement()
    val spriteElement = SpriteElement(Res.cardSheet.createSprite(gameCard.getSpriteSheetPosition()))

    init{
        uiNode.addContainer(VContainer(spriteElement)){ uiNode.bounds }
        uiNode.addContainer(VContainer(squareElement)){ uiNode.bounds }
    }
}