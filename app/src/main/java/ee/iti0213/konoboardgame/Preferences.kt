package ee.iti0213.konoboardgame

object Preferences {

    private var blueStarts = false
    private var blueAI = false
    private var orangeAI = false

    fun getBlueStarts(): Boolean {
        return this.blueStarts
    }

    fun setBlueStarts(blueStarts: Boolean) {
        this.blueStarts = blueStarts
    }

    fun getBlueAI(): Boolean {
        return this.blueAI
    }

    fun setBlueAI(blueAI: Boolean) {
        this.blueAI = blueAI
    }

    fun getOrangeAI(): Boolean {
        return this.orangeAI
    }

    fun setOrangeAI(orangeAI: Boolean) {
        this.orangeAI = orangeAI
    }
}