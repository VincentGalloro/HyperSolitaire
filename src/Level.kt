import data.Game
import data.moves.ColumnTransferMove
import data.moves.DealMove
import data.moves.PutUpMove
import ui.GameNode
import ui.GameStateNode
import velvet.main.VGraphics
import velvet.structs.Bounds
import velvet.structs.Vector
import velvet.ui.UIEventHandler
import velvet.ui.premade.components.BasicComponent
import java.awt.event.KeyEvent

class Level(val uiEventHandler: UIEventHandler) {

    val game = Game.createNewGame()

    val gameNode = GameNode(game)

    init{
        gameNode.uiNode.bounds = Bounds.fromStartOfSize(Vector(), Vector(1800, 1000))

        uiEventHandler.root = gameNode.uiNode
    }

    fun update(){
        uiEventHandler.root?.update()
    }

    fun render(g: VGraphics){
        uiEventHandler.root?.render(g)
    }
}