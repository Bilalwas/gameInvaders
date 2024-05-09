package com.example.gameinvaders

import android.graphics.Bitmap


class SpaceshipRocket(bitmap: Bitmap, x: Float, y: Float) : Rocket(bitmap, x, y) {
    override val speed = 20f

    override fun update() {
        y -= speed
        if (y < 0) { // Sortie par le haut de l'écran
            isVisible = false
        }
    }
}
