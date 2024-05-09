package com.example.gameinvaders

import android.graphics.Bitmap



class InvaderRocket(bitmap: Bitmap, x: Float, y: Float) : Rocket(bitmap, x, y) {
    override val speed = 10f

    override fun update() {
        y += speed
        if (y > 1920) { // Remplace par la hauteur effective de l'écran
            isVisible = false
        }
    }
}

