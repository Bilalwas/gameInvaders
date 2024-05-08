package com.example.gameinvaders

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class InvaderSpeedy(context: Context, screenX: Int, screenY: Int) : Invader(context, screenX, screenY) {
    override var invadersBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.speedy_invader_image_background)
    override var speed = 2f
}