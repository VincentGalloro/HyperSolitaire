package data.game

import data.card.Card
import velvet.structs.Position

class GameCard(val card: Card, val visible: Boolean){

    fun reveal() = GameCard(card, visible = true)

    override fun toString() = if(visible) card.toString() else "XX"
    fun getSpriteSheetPosition() = if(visible) card.getSpriteSheetPosition() else Position(0, 4)
    fun getUID() = "${if(visible) "" else "X"}$card"
}