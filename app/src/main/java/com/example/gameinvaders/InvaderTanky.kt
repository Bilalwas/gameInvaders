package com.example.gameinvaders

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class InvaderTanky(context: Context, screenX: Int, screenY: Int) : Invader(context, screenX, screenY) {
    override var health: Int = 1   // Plus de santé
    override var invadersBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.tanky_invader_image_background)


    override fun update() {
        // Peut-être se déplacer plus lentement mais avec plus de santé
        super.update()
        y += 1 // Se déplace plus lentement //
    }
}

