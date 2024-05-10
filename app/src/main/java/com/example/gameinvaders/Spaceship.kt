package com.example.gameinvaders

import android.graphics.Bitmap
import android.graphics.RectF
import android.view.MotionEvent


// Spaceship.kt
class Spaceship(val bitmap: Bitmap, override var x: Float, override var y: Float,private val rockets: MutableList<SpaceshipRocket>,private val invaders :  Invaders, var score : Observable<Int>): GameEntity {
    val speed = 20f // Vitesse de déplacement du vaisseau
    var health = Observable(3)// Points de vie du vaisseau
    var rocketDamage = 1
    fun getCollisionRect(): RectF {
        return RectF(x, y, x + bitmap.width, y + bitmap.height)
    }
    override fun update() {
        // Vous pouvez ajouter la logique d'update du spaceship ici si nécessaire
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
        health.set(health.get() - damage)
    }
    fun launchRocket(rocketBitmap: Bitmap) {
        // Crée une nouvelle roquette à la position actuelle du vaisseau
        val newRocket = SpaceshipRocket(rocketBitmap, x , y)
        rockets.add(newRocket)
    }
    fun checkCollisions(bonuses: List<Bonus>) {
        val spaceshipRect = getCollisionRect()

        // Vérifier les collisions avec chaque invader
        invaders.invadersList.forEach { invader ->
            if (invader.isVisible) {
                val invaderRect = invader.getCollisionRect()
                if (RectF.intersects(spaceshipRect, invaderRect)) {
                    // Collision entre le vaisseau et un invader
                    takeDamage(1)
                    invader.isVisible = false
                    score.set(score.get() + invaders.scorePerInvader)
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
        bonuses.forEach { bonus ->
            val bonusRect = bonus.getCollisionRect()
            if (RectF.intersects(spaceshipRect, bonusRect) && bonus.isVisible) {
                bonus.applyEffect(this)
                bonus.isVisible = false
            }
        }
    }
}