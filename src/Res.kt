import velvet.main.game.graphics.SpriteSheet
import velvet.structs.Position
import java.nio.file.Path


class Res {

    companion object{

        val cardSheet = SpriteSheet.loadSpriteSheet(Path.of("cards.png"), Position(124, 180))
    }
}