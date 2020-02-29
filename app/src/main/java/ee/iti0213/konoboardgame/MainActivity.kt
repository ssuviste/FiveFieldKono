package ee.iti0213.konoboardgame

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun changeButtonColor(view: View, colorList1: ColorStateList?, colorList2: ColorStateList?) {
        when (ViewCompat.getBackgroundTintList(view)) {
            colorList1 ->
                ViewCompat.setBackgroundTintList(view, colorList2)
            colorList2 ->
                ViewCompat.setBackgroundTintList(view, colorList1)
            else -> return
        }
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
