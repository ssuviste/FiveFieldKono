package ee.iti0213.konoboardgame

enum class GameState(val msg: String?) {
    BLUE_TURN(null),
    ORANGE_TURN(null),
    BLUE_WIN("Blue wins!"),
    ORANGE_WIN("Orange wins!")
}