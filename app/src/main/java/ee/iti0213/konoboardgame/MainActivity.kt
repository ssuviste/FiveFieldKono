package ee.iti0213.konoboardgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.muddzdev.styleabletoast.StyleableToast
import kotlinx.android.synthetic.main.game_stats.*
import kotlinx.android.synthetic.main.game_stats.view.*

class MainActivity : AppCompatActivity() {

    private var helpBtnLastClickTime = 0L
    private var prefBtnLastClickTime = 0L

    private lateinit var handler: Handler
    private val doAITurnCheck = object : Runnable {
        override fun run() {
            if (GameEngine.isAITurn()) {
                val msg = GameEngine.doAIMove()
                if (msg != null) showToastMsg(msg)
                updateUI()
            }
            handler.postDelayed(this, C.AI_CHECK_INTERVAL)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        handler = Handler(Looper.getMainLooper())
    }

    override fun onPause() {
        super.onPause()

        handler.removeCallbacks(doAITurnCheck)
    }

    override fun onResume() {
        super.onResume()

        updateUI()

        if (Preferences.orangeAI || Preferences.blueAI) {
            handler.postDelayed(doAITurnCheck, C.AI_CHECK_INTERVAL)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(C.RST_BTN_STATE_KEY, buttonRestart.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        buttonRestart.text = savedInstanceState.getString(
            C.RST_BTN_STATE_KEY,
            R.string.start_btn.toString()
        )
    }

    fun buttonBoardOnClick(view: View) {
        val msg = GameEngine.doPlayerMove(view.tag.toString())
        if (msg != null) showToastMsg(msg)

        updateUI()
    }

    fun buttonRestartOnClick(view: View) {
        GameEngine.resetGame()
        view.buttonRestart.text = getString(R.string.rst_btn)

        updateUI()
    }

    @Suppress("UNUSED_PARAMETER")
    fun buttonHelpOnClick(view: View) {
        if (SystemClock.elapsedRealtime() - helpBtnLastClickTime < C.BTN_COOL_DOWN) {
            return
        }
        helpBtnLastClickTime = SystemClock.elapsedRealtime()

        val intent = Intent(this, HelpActivity::class.java)
        startActivity(intent)
    }

    @Suppress("UNUSED_PARAMETER")
    fun buttonPreferencesOnClick(view: View) {
        if (SystemClock.elapsedRealtime() - prefBtnLastClickTime < C.BTN_COOL_DOWN) {
            return
        }
        prefBtnLastClickTime = SystemClock.elapsedRealtime()

        val intent = Intent(this, PreferencesActivity::class.java)
        startActivity(intent)
    }

    private fun updateUI() {
        updateBoard()
        updatePlayerIcons()
        updatePlayerScores()
    }

    private fun updateBoard() {
        val gameBoard = GameEngine.getGameBoard()
        for (y in gameBoard.indices) {
            for (x in gameBoard[y].indices) {
                val buttonId = resources.getIdentifier("button$y$x", "id", packageName)
                val button = findViewById<Button>(buttonId)
                val colorId = gameBoard[y][x].colorId
                ViewCompat.setBackgroundTintList(
                    button, ContextCompat.getColorStateList(this, colorId)
                )
            }
        }
    }

    private fun updatePlayerIcons() {
        val orange = ContextCompat.getColor(this, Color.ORANGE.colorId)
        val orangeH = ContextCompat.getColor(this, Color.ORANGE_H.colorId)
        val blue = ContextCompat.getColor(this, Color.BLUE.colorId)
        val blueH = ContextCompat.getColor(this, Color.BLUE_H.colorId)
        val mode = android.graphics.PorterDuff.Mode.MULTIPLY

        when (GameEngine.getGameState()) {
            GameState.ORANGE_TURN -> {
                player1Icon.setColorFilter(orangeH, mode)
                player2Icon.setColorFilter(blue, mode)
            }
            GameState.BLUE_TURN -> {
                player1Icon.setColorFilter(orange, mode)
                player2Icon.setColorFilter(blueH, mode)
            }
            else -> {
                player1Icon.setColorFilter(orange, mode)
                player2Icon.setColorFilter(blue, mode)
            }
        }
    }

    private fun updatePlayerScores() {
        findViewById<TextView>(player1Score.id).text =
            if (GameEngine.getOrangeScore() > C.MAX_SCORE)
                C.MAX_SCORE.toString()
            else GameEngine.getOrangeScore().toString()
        findViewById<TextView>(player2Score.id).text =
            if (GameEngine.getBlueScore() > C.MAX_SCORE)
                C.MAX_SCORE.toString()
            else GameEngine.getBlueScore().toString()
    }

    private fun showToastMsg(msg: String) {
        StyleableToast.makeText(this, msg, Toast.LENGTH_LONG, R.style.glassToast).show()
    }
}