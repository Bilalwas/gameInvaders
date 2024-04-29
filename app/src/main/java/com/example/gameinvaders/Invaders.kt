package com.example.gameinvaders

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import com.example.gameinvaders.R

class Invader(context: Context, val screenX: Int, val screenY: Int) {
    var invadersBitmap: Bitmap
    var x: Float = 0f
    var y: Float = 0f
    var isVisible = true
    private var health: Int = 3 // Ajouté : points de vie de l'invader


    init {
        invadersBitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.invader_image_background)
        // Ajustez la taille de l'invader ici si nécessaire
        x = screenX / 2f
        y = screenY / 2f

    }
    fun getCollisionRect(): RectF {
        return RectF(x, y, x + invadersBitmap.width, y + invadersBitmap.height)
    }
    fun takeDamage(damage: Int) {
        health -= damage
        if (health <= 0) {
            isVisible = false
            // L'invader est détruit s'il n'a plus de points de vie
        }
    }
    fun update() {
        // Logique de déplacement des invaders
    }

    // Ajoutez d'autres méthodes nécessaires, comme une méthode pour détecter les collisions
}
