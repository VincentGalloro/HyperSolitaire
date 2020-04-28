package data

import data.game.GameState
import data.moves.Move

class Timeline(val initialState: GameState) {

    private val stateUIDs: MutableMap<String, GameState> = mutableMapOf()
    private val stateTransitions: MutableMap<GameState, MutableList<Pair<Move, GameState>>> = mutableMapOf()
    private val parentTransitions: MutableMap<GameState, Pair<GameState, Move>> = mutableMapOf()

    init{
        stateUIDs[initialState.getUID()] = initialState
    }

    fun getGameStates() = stateUIDs.values.toList()

    fun createTransition(gameState: GameState, move: Move): GameState?{
        val newState = move.makeMove(gameState) ?: return null
        stateUIDs[newState.getUID()]?.let { return it }

        stateTransitions.getOrPut(gameState){ mutableListOf() }.add(move to newState)
        parentTransitions[newState] = gameState to move

        stateUIDs[newState.getUID()] = newState

        return newState
    }
    fun getTransitions(gameState: GameState) = stateTransitions[gameState] ?: mutableListOf()
    fun getParentTransition(gameState: GameState) = parentTransitions[gameState]

    operator fun contains(gameState: GameState) = gameState.getUID() in stateUIDs
}