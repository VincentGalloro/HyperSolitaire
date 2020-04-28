package data.game

import data.card.Card
import data.card.CardSuit
import data.card.CardValue
import java.lang.Integer.min

data class DealResult(val newDeck: Deck, val dealtCards: List<Card>)

class Deck(private val cards: List<Card>){

    companion object{

        fun createNewDeck() = Deck(CardSuit.values().flatMap { suit ->
            CardValue.values().flatMap { value -> List(2){ Card(value, suit) } }
        })
        fun createShuffledDeck() = createNewDeck().shuffle()
    }

    fun shuffle() = Deck(cards.shuffled())

    fun deal(amount: Int) = min(amount, cards.size).let { safeAmount ->
        DealResult(Deck(cards.subList(safeAmount, cards.size)), cards.subList(0, safeAmount))
    }

    fun getUID() = cards.map { it.getUID() }.toString()
}