package com.example.gameinvaders

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF

class Invaders(
    val context: Context,
    val screenWidth: Int,
    val screenHeight: Int,
    val invaderBitmap: Bitmap,
    val rockets: MutableList<InvaderRocket>,
) {
    val invadersList = mutableListOf<Invader>()
    private var invaderType = 0
    private var direction = 1
    private var invaderSpeed = 1f
    var scorePerInvader: Int = 500
    val numRows: Int = 5
    val numCols: Int = 6

    init {
        createInvaderTroop()
    }

    fun createInvaderTroop() {
        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                val invader = createInvaderType()
                invader.apply {
                    x = col * (invader.invadersBitmap.width / 2 + 1f)
                    y = row * (invader.invadersBitmap.height / 2 + 1f)
                    invadersList.add(this)
                }
            }
        }
        invaderType++
    }

    private fun createInvaderType(): Invader {
        return when (invaderType % 3) {
            0 -> RocketInvader(context, screenWidth, screenHeight, rockets)
            1 -> TankyInvader(context, screenWidth, screenHeight)
            else -> SpeedyInvader(context, screenWidth, screenHeight)
        }
    }

    fun updateInvaders(screenWidth: Int, invaderrocketBitmap:Bitmap) {
        var edgeReached = false

        invadersList.forEach { invader ->
            invader.x += invaderSpeed * direction

            // Vérifier si un invader atteint le bord de l'écran
            if (invader.x > screenWidth - invader.invadersBitmap.width || invader.x < 0) {
                edgeReached = true
            }
            if (invader is RocketInvader) {
                invader.attemptToShoot(invaderrocketBitmap) // Appeler une méthode qui décide quand tirer
            }
        }

        if (edgeReached) {
            // Inverser la direction
            direction *= -1

            // Descendre d'une "ligne"
            invadersList.forEach { invader ->
                invader.y += invader.invadersBitmap.height / 10
            }
        }
    }
    fun resetInvaders() {
        invadersList.clear()
        createInvaderTroop()
    }
    fun removeDestroyedInvaders() {
        invadersList.removeIf { !it.isVisible }
    }

    fun checkCollisions(spaceshipRockets: MutableList<SpaceshipRocket>,score: Observable<Int> ) {
        invadersList.forEach { invader ->
            if (invader.isVisible) {
                val invaderRect = invader.getCollisionRect()
                spaceshipRockets.forEach { rocket ->
                    val rocketRect = rocket.getCollisionRect()
                    if (RectF.intersects(invaderRect, rocketRect) && rocket.isVisible) {
                        invader.takeDamage(1)
                        rocket.isVisible = false
                        if (!invader.isVisible) {
                            score.set(score.get() + 500)
                        }
                        rocket.isVisible = false
                    }
                }
            }
        }
    }
    fun allInvadersDestroyed(): Boolean {
        return invadersList.all { !it.isVisible }
    }
}
