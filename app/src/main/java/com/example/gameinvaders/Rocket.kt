package com.example.gameinvaders

import android.graphics.Bitmap
import android.graphics.RectF

class Rocket(val bitmap: Bitmap, var x: Float, var y: Float) {
    var isVisible = true
    var speed = 20f
    fun getCollisionRect(): RectF {
        return RectF(x, y, x + bitmap.width, y + bitmap.height)
    }
    fun update() {
        y -= speed
        if (y < 0) { // Si la roquette sort de l'écran, elle devient invisible
            isVisible = false
        }
    }
}