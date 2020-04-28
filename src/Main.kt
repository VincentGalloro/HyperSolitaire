import velvet.main.VGraphics
import velvet.main.Velvet
import velvet.structs.Position

class Main : Velvet(Position(1800, 1000)){

    lateinit var level: Level

    override fun init() {
        level = Level(uiEventHandler)
    }

    override fun onClose() {
    }

    override fun render(g: VGraphics) {
        level.render(g)
    }

    override fun update() {
        level.update()
    }

}

fun main(){
    Velvet.start(Main(), "HyperSolitaire")
}