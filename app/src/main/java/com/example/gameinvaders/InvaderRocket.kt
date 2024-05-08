package com.example.gameinvaders

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory


class InvaderRocket(context: Context, screenX: Int, screenY: Int, private val rocketlist: MutableList<Rocket>) : Invader(context, screenX, screenY) {
    override var invadersBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.rocket_invader_image_background)
    val rocketBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.rocket_image_background)



    override fun update() {
        super.update()
    }

    fun shoot() {
            val newRocket = Rocket(rocketBitmap, x + invadersBitmap.width / 2 - rocketBitmap.width / 2, y)
            rocketlist.add(newRocket)

    }
}
