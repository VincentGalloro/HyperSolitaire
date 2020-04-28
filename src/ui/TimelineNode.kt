package ui

import data.Timeline
import data.game.GameState
import velvet.structs.Bounds
import velvet.structs.Position
import velvet.structs.VColor
import velvet.structs.Vector
import velvet.ui.UINode
import velvet.ui.premade.components.BasicComponent
import velvet.ui.vcontainer.VContainer
import velvet.ui.vcontainer.velements.SquareElement
import java.lang.Integer.max

class TimelineNode {

    val uiNode = UINode()

    var onStateSelected: ((GameState)->Unit)? = null

    init{
        uiNode.addContainer(VContainer(SquareElement(VColor.WHITE))){ uiNode.bounds }
    }

    private fun createNodePositions(timeline: Timeline,
                                    gameState: GameState,
                                    position: Position = Position()): Pair<Map<GameState, Position>, Int>{
        var out = mapOf(gameState to position)
        var yOffset = 0
        timeline.getTransitions(gameState).forEachIndexed { index, (_, newState) ->
            val (positions, height) = createNodePositions(timeline, newState, position+Position(1, yOffset))
            out = out + positions
            yOffset += height
        }
        return out to max(yOffset, 1)
    }

    fun loadTimeline(timeline: Timeline, currentState: GameState){
        val nodePositions = createNodePositions(timeline, timeline.initialState).first.mapValues {
            uiNode.bounds.getPos(Vector())+30 + it.value.toVector()*40
        }

        uiNode.subNodes.clear()

        uiNode.subNodes.addAll(timeline.getGameStates().map { gameState ->
            UINode().also {
                timeline.getTransitions(gameState).forEach { (move, newState) ->
                    it.addContainer(VContainer(SquareElement(outlineColor = move.color))){
                        Bounds.fromStartToEnd(nodePositions[gameState] ?: Vector(),
                            nodePositions[newState] ?: Vector()).setWidth(0.0, 0.0)
                    }
                    it.addContainer(VContainer(SquareElement(outlineColor = move.color))){
                        Bounds.fromStartToEnd(nodePositions[gameState] ?: Vector(),
                            nodePositions[newState] ?: Vector()).setHeight(0.0, 1.0)
                    }
                }
                it.addContainer(VContainer(SquareElement(
                    if(gameState==currentState) VColor(255,0,0) else VColor.WHITE,
                    outlineColor = timeline.getParentTransition(gameState)?.second?.color ?: VColor.BLACK))){ it.bounds }

                it.bounds = Bounds.fromCenterOfSize(
                    nodePositions[gameState] ?: Vector(),
                    Vector(20))
                it.uiComponents.add(object : BasicComponent(){
                    init{
                        uiEventListener.onMouseHover = { onStateSelected?.invoke(gameState) }
                    }
                })
            }
        })
    }
}