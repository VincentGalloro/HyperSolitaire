package data.card

import velvet.structs.Position

class Card(val value : CardValue, val suit: CardSuit){

    fun canStackOn(card: Card) = card.value.ordinal == value.ordinal+1 && card.suit.color != suit.color

    override fun toString() = "${value.symbol}${suit.name[0]}"
    fun getSpriteSheetPosition() = Position(value.ordinal, suit.spriteSheetY)
    fun getUID() = toString()
}