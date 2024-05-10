package com.example.gameinvaders

import android.graphics.Bitmap

class DamageBonus(bitmap: Bitmap, x: Float, y: Float) : Bonus(bitmap, x, y) {
    override fun applyEffect(spaceship: Spaceship) {
        // Augmente les dégâts infligés par les roquettes du vaisseau
        spaceship.rocketDamage += 1
        // Le bonus n'est plus visible après utilisation
        isVisible = false
    }
}