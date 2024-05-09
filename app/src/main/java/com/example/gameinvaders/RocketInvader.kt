package com.example.gameinvaders

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.Random


class RocketInvader(context: Context, screenX: Int, screenY: Int, private val rocketlist: MutableList<InvaderRocket>) : Invader(context, screenX, screenY) {
    override var invadersBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.rocket_invader_image_background)
    private var lastShotTime = System.currentTimeMillis()
    private val random = Random()
    private val shootingInterval = random.nextInt(100000) + 2000 // Intervalle de tir aléatoire entre 2s et 5s



    override fun update() {
        super.update()
    }
    fun attemptToShoot(rocketBitmap:Bitmap) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastShotTime >= shootingInterval) {
            shoot(rocketBitmap)
            lastShotTime = currentTime
        }
    }

    private fun shoot(rocketBitmap:Bitmap) {
        // Crée une nouvelle roquette
        val newRocket = InvaderRocket(rocketBitmap, x + invadersBitmap.width / 2 - rocketBitmap.width / 2, y)
        rocketlist.add(newRocket)
    }
}
