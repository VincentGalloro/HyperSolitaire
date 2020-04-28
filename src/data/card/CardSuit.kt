package data.card

enum class SuitColor{
    BLACK,
    RED,
}

enum class CardSuit(val color: SuitColor, val spriteSheetY: Int) {
    CLUB(SuitColor.BLACK, 2),
    SPADE(SuitColor.BLACK, 3),
    DIAMOND(SuitColor.RED, 1),
    HEART(SuitColor.RED, 0),
}