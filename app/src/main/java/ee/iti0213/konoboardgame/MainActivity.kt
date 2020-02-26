package ee.iti0213.konoboardgame

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun boardButtonOnClick(view: View) {
        val buttonId = findViewById<Button>(view.id)
        when (buttonId.backgroundTintList) {
            getColorStateList(R.color.colorBlue) ->
                buttonId.backgroundTintList = getColorStateList(R.color.colorBlueHighlighted)
            getColorStateList(R.color.colorBlueHighlighted) ->
                buttonId.backgroundTintList = getColorStateList(R.color.colorBlue)
            getColorStateList(R.color.colorOrange) ->
                buttonId.backgroundTintList = getColorStateList(R.color.colorOrangeHighlighted)
            getColorStateList(R.color.colorOrangeHighlighted) ->
                buttonId.backgroundTintList = getColorStateList(R.color.colorOrange)
        }
    }
}
