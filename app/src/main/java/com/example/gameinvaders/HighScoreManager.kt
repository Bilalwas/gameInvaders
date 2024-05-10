// HighScoreManager.kt
package com.example.gameinvaders

import android.content.Context
import android.content.SharedPreferences

object HighScoreManager {
    private const val PREFS_NAME = "high_score_prefs"
    private const val KEY_HIGH_SCORE = "high_score"

    private lateinit var sharedPreferences: SharedPreferences

    // Initialisation avec le contexte de l'application
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Récupérer le high score depuis les SharedPreferences
    fun getHighScore(): Int {
        return sharedPreferences.getInt(KEY_HIGH_SCORE, 0)
    }

    // Définir un nouveau high score
    fun setHighScore(score: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_HIGH_SCORE, score)
        editor.apply()
    }
}
