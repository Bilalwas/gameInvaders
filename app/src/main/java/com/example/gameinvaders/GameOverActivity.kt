package com.example.gameinvaders

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class GameOverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        val restartButton: Button = findViewById(R.id.restartButton)
        restartButton.setOnClickListener {
            // Lancer l'activité principale pour redémarrer le jeu
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            // Fermer l'activité Game Over
            finish()
        }
    }
}
