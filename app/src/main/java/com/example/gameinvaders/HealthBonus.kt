package com.example.gameinvaders

import android.graphics.Bitmap

class HealthBonus(bitmap: Bitmap, x: Float, y: Float) : Bonus(bitmap, x, y) {
    override fun applyEffect(spaceship: Spaceship) {
        // Ajoute un point de vie supplémentaire au vaisseau
        spaceship.health.set(spaceship.health.get() + 1)
        // Le bonus n'est plus visible après utilisation
        isVisible = false
    }
}