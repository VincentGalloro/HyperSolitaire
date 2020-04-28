package data.game

import data.card.CardValue

data class StackTransferOutResult(val newCardStack: CardStack, val card: GameCard)

class CardStack(val gameCards: List<GameCard>){
    companion object{
        fun empty() = CardStack(emptyList())
    }

    fun transferCardIn(gameCard: GameCard): CardStack?{
        if(!gameCard.visible) return null
        if(gameCards.isEmpty() && gameCard.card.value != CardValue.ACE) return null
        if(gameCards.isNotEmpty() && (gameCard.card.suit != top()?.card?.suit ||
                    gameCard.card.value.ordinal != top()?.card?.value?.ordinal?.plus(1))) return null
        return CardStack(gameCards + listOf(gameCard))
    }
    fun transferCardOut(): StackTransferOutResult?{
        return top()?.let {
            StackTransferOutResult(CardStack(gameCards.subList(0, gameCards.size-1)), it)
        }
    }

    fun top() = gameCards.lastOrNull()
    fun getUID() = gameCards.map { it.getUID() }.toString()
}