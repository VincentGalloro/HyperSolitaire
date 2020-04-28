package data.game

data class ColumnTransferOutResult(val newCardColumn: CardColumn, val cards: List<GameCard>)
data class ColumnSingleTransferOutResult(val newCardColumn: CardColumn, val card: GameCard)

class CardColumn private constructor(val gameCards: List<GameCard>){
    companion object{
        fun empty() = CardColumn(emptyList())
    }

    fun addCard(gameCard: GameCard) = CardColumn(gameCards + gameCard)

    private fun revealTop() = CardColumn(gameCards.map { if(it===gameCards.lastOrNull()) it.reveal() else it })

    fun isLegalStackFrom(index: Int): Boolean{
        for(i in index until gameCards.size){ if(!gameCards[i].visible) return false }
        for(i in index until gameCards.size-1){
            if(!gameCards[i+1].card.canStackOn(gameCards[i].card)) return false
        }
        return true
    }

    fun transferStackIn(stackCards: List<GameCard>): CardColumn? {
        if(gameCards.isNotEmpty() && !stackCards.first().card.canStackOn(gameCards.last().card)) return null //Stack cannot be transferred on
        return CardColumn(gameCards + stackCards)
    }
    fun transferStackOut(stackCardRoot: GameCard) : ColumnTransferOutResult?{
        val index = gameCards.indexOf(stackCardRoot)
        if(index == -1) return null //StackCard not in column
        if(!isLegalStackFrom(index)) return null //Illegal stack cannot be moved
        return ColumnTransferOutResult(CardColumn(gameCards.subList(0, index)).revealTop(),
            gameCards.subList(index, gameCards.size))
    }
    fun transferTopOff(): ColumnSingleTransferOutResult? {
        return top()?.let {
            return ColumnSingleTransferOutResult(CardColumn(gameCards.subList(0, gameCards.size-1)).revealTop(), it)
        }
    }

    fun top() = gameCards.lastOrNull()

    override fun toString() = gameCards.toString()
    fun getUID() = gameCards.map { it.getUID() }.toString()
}