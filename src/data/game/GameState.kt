package data.game

class GameState(val cardColumns: List<CardColumn>,
                val cardStacks: List<CardStack>,
                val deck: Deck){

    companion object{
        private const val DEFAULT_COLUMN_COUNT = 8
        private const val DEFAULT_STACK_COUNT = 8

        fun newGameState(deck: Deck) = GameState(
            List(DEFAULT_COLUMN_COUNT){ CardColumn.empty() },
            List(DEFAULT_STACK_COUNT){ CardStack.empty() },
            deck)
    }

    fun getUID() = "${cardColumns.map { it.getUID() }} " +
            "${cardStacks.map { it.getUID() }} ${deck.getUID()}"
}