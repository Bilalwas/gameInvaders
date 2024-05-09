package com.example.gameinvaders

import android.graphics.Bitmap
import android.graphics.RectF

abstract class Rocket(val bitmap: Bitmap, var x: Float, var y: Float) {
    var isVisible = true
    abstract val speed: Float

    abstract fun update()

    fun getCollisionRect(): RectF {
        return RectF(x, y, x + bitmap.width, y + bitmap.height)
    }
}
