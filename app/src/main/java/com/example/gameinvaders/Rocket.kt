package com.example.gameinvaders

import android.graphics.Bitmap
import android.graphics.RectF

abstract class Rocket(val bitmap: Bitmap, override var x: Float, override var y: Float) : GameEntity{
    var isVisible = true
    abstract val speed: Float

    override abstract fun update()

    fun getCollisionRect(): RectF {
        return RectF(x, y, x + bitmap.width, y + bitmap.height)
    }
}
