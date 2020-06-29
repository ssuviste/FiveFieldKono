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

        switchBlueStarts.isChecked = Preferences.blueStarts
        switchBlueAI.isChecked = Preferences.blueAI
        switchOrangeAI.isChecked = Preferences.orangeAI

        switchBlueStarts.setOnCheckedChangeListener { _, isChecked ->
            Preferences.blueStarts = isChecked
        }

        switchBlueAI.setOnCheckedChangeListener { _, isChecked ->
            Preferences.blueAI = isChecked
        }

        switchOrangeAI.setOnCheckedChangeListener { _, isChecked ->
            Preferences.orangeAI = isChecked
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun prefScreenCloseOnClick(view: View) {
        finish()
    }
}