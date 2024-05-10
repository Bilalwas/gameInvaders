package com.example.gameinvaders

import android.graphics.Bitmap
import android.graphics.RectF

abstract class Bonus(val bitmap: Bitmap, override var x: Float, override var y: Float) : GameEntity{
    var isVisible = true
    var speed = 5f

    override fun update() {
        // Déplace le bonus vers le bas de l'écran
        y += speed
        // Si le bonus sort de l'écran, il devient invisible
        if (y > 1000f) {
            isVisible = false
        }
    }

    // Vérifie la collision entre le bonus et une autre entité
    fun getCollisionRect(): RectF {
        return RectF(x, y, x + bitmap.width, y + bitmap.height)
    }

    // Méthode abstraite à implémenter pour définir l'effet du bonus
    abstract fun applyEffect(spaceship: Spaceship)
}
