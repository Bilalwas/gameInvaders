package com.example.gameinvaders

// MainActivity.kt
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var gameView: GameView
    private var isGamePaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Utilisez seulement une fois
        HighScoreManager.init(this)
        gameView = findViewById(R.id.game_view)

        val launchRocketButton = findViewById<Button>(R.id.launch_rocket_button)
        launchRocketButton.setOnClickListener {
            gameView.spaceship.launchRocket(gameView.spaceshiprocketBitmap) }
        val pauseButton: Button = findViewById(R.id.pauseButton)
        pauseButton.setOnClickListener {
            if (isGamePaused) {
                gameView.resumeGame()
                pauseButton.text = "Pause"
            }
            else {
                gameView.pauseGame()
                pauseButton.text = "Resume"
            }
            isGamePaused = !isGamePaused
        }

    }
}


