package ee.iti0213.konoboardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View

class HelpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels * 0.8
        val height = dm.heightPixels * 0.6
        window.setLayout(width.toInt(), height.toInt())
    }

    fun helpScreenCloseOnClick(view: View) {
        finish()
    }
}
