package com.example.gameinvaders

import android.graphics.Bitmap
import android.graphics.RectF
import android.view.MotionEvent


// Spaceship.kt
class Spaceship(val bitmap: Bitmap, var x: Float, var y: Float,private val rockets: MutableList<SpaceshipRocket>,private val invaders :  Invaders, var score : Int) {
    val speed = 20f // Vitesse de déplacement du vaisseau
    var health: Int = 3 // Points de vie du vaisseau
    fun getCollisionRect(): RectF {
        return RectF(x, y, x + bitmap.width, y + bitmap.height)
    }
    fun moveLeft() {
        x += speed
    }
    fun moveRight() {
        x += speed
    }
    fun updateBounds(maxWidth: Int, margin: Float) {
        x = x.coerceIn(margin, maxWidth - bitmap.width - margin)
    }

    fun takeDamage(damage: Int) {
        health -= damage
        if (health <= 0) {
            // Gérer le cas où le vaisseau est détruit
        }
    }
    fun launchRocket(rocketBitmap: Bitmap) {
        // Crée une nouvelle roquette à la position actuelle du vaisseau
        val newRocket = SpaceshipRocket(rocketBitmap, x , y)
        rockets.add(newRocket)
    }
    fun checkCollisions() {
        val spaceshipRect = getCollisionRect()

        // Vérifier les collisions avec chaque invader
        invaders.invadersList.forEach { invader ->
            if (invader.isVisible) {
                val invaderRect = invader.getCollisionRect()
                if (RectF.intersects(spaceshipRect, invaderRect)) {
                    // Collision entre le vaisseau et un invader
                    takeDamage(1)
                    invader.isVisible = false
                    score += invaders.scorePerInvader
                }
            }
        }

        // Vérifier les collisions avec les roquettes des invaders
        invaders.rockets.forEach { rocket ->
            val rocketRect = rocket.getCollisionRect()
            if (RectF.intersects(spaceshipRect, rocketRect)) {
                // Collision entre le vaisseau et une roquette d'invader
                takeDamage(1)
                rocket.isVisible = false
            }
        }
    }
}