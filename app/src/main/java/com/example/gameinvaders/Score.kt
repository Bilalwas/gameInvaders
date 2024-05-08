package com.example.gameinvaders

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface

class Score {
    private var score = 0
    private val scorePaint = Paint().apply {
        color = Color.BLUE
        textSize = 60f
        typeface = Typeface.DEFAULT_BOLD
    }

    fun increase(amount: Int) {
        score += amount
    }

    fun reset() {
        score = 0
    }

    fun draw(canvas: Canvas) {
        canvas.drawText("Score: $score", 50f, 100f, scorePaint)
    }

    fun getScore(): Int {
        return score
    }
}
