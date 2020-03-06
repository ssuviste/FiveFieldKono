package ee.iti0213.konoboardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import kotlinx.android.synthetic.main.preferences.*

class PreferencesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels * 0.8
        val height = dm.heightPixels * 0.6
        window.setLayout(width.toInt(), height.toInt())

        switchBlueStarts.isChecked = Preferences.getBlueStarts()
        switchBlueAI.isChecked = Preferences.getBlueAI()
        switchOrangeAI.isChecked = Preferences.getOrangeAI()

        switchBlueStarts.setOnCheckedChangeListener { _, isChecked ->
            Preferences.setBlueStarts(isChecked)
        }

        switchBlueAI.setOnCheckedChangeListener { _, isChecked ->
            Preferences.setBlueAI(isChecked)
        }

        switchOrangeAI.setOnCheckedChangeListener { _, isChecked ->
            Preferences.setOrangeAI(isChecked)
        }
    }

    fun prefScreenCloseOnClick(view: View) {
        finish()
    }
}