package com.example.gameinvaders

import android.graphics.Bitmap
import android.graphics.RectF
import android.view.MotionEvent


// Spaceship.kt
class Spaceship(val bitmap: Bitmap, var x: Float, var y: Float,private val rockets: MutableList<Rocket>,private val invaders :  MutableList<Invader>) {
    val speed = 20f // Vitesse de déplacement du vaisseau
    var health: Int = 3 // Points de vie du vaisseau

    fun getCollisionRect(): RectF {
        return RectF(x, y, x + bitmap.width, y + bitmap.height)
    }
    

    fun takeDamage(damage: Int) {
        health -= damage
        if (health <= 0) {
            // Gérer le cas où le vaisseau est détruit
        }
    }
}