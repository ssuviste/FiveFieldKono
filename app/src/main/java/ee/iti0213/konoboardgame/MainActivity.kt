package ee.iti0213.konoboardgame

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.game_stats.*

class MainActivity : AppCompatActivity() {

    companion object {
        var moves = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // Just for testing UI color change
    private fun changeButtonColor(view: View, colorList1: ColorStateList?, colorList2: ColorStateList?) {
        when (ViewCompat.getBackgroundTintList(view)) {
            colorList1 ->
                ViewCompat.setBackgroundTintList(view, colorList2)
            colorList2 ->
                ViewCompat.setBackgroundTintList(view, colorList1)
            else -> return
        }
    }

    // Just for testing UI color change
    private fun swapPlayerHighlights() {
        val orange = ContextCompat.getColor(this, R.color.colorOrange)
        val orangeH = ContextCompat.getColor(this, R.color.colorOrangeHighlighted)
        val blue = ContextCompat.getColor(this, R.color.colorBlue)
        val blueH = ContextCompat.getColor(this, R.color.colorBlueHighlighted)
        val mode = android.graphics.PorterDuff.Mode.MULTIPLY

        val player1Icon = findViewById<ImageView>(player1Color.id)
        val player2Icon = findViewById<ImageView>(player2Color.id)

        when (moves % 2) {
            0 -> {
                player1Icon.setColorFilter(orangeH, mode)
                player2Icon.setColorFilter(blue, mode)
            }
            1 -> {
                player1Icon.setColorFilter(orange, mode)
                player2Icon.setColorFilter(blueH, mode)
            }
        }
        moves++
    }

    fun buttonBoardOnClick(view: View) {
        val orange = ContextCompat.getColorStateList(this, R.color.colorOrange)
        val orangeH = ContextCompat.getColorStateList(this, R.color.colorOrangeHighlighted)
        val blue = ContextCompat.getColorStateList(this, R.color.colorBlue)
        val blueH = ContextCompat.getColorStateList(this, R.color.colorBlueHighlighted)

        when (ViewCompat.getBackgroundTintList(view)) {
            blue, blueH -> changeButtonColor(view, blue, blueH)
            orange, orangeH -> changeButtonColor(view, orange, orangeH)
        }

        swapPlayerHighlights()
    }

    fun buttonHelpOnClick(view: View) {
        val intent = Intent(this, HelpActivity::class.java)
        startActivity(intent)
    }

    fun buttonAIOnClick(view: View) {
        val glassFrosted = ContextCompat.getColorStateList(this, R.color.colorGlassFrosted)
        val greenH = ContextCompat.getColorStateList(this, R.color.colorGreenHighlighted)
        changeButtonColor(view, glassFrosted, greenH)
    }
}
