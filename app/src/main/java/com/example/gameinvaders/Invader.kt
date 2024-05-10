package com.example.gameinvaders

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import com.example.gameinvaders.R

open class Invader(context: Context, val screenX: Int, val screenY: Int) : GameEntity {
    open var invadersBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.invader_image_background)
    override var x: Float = 0f
    override var y: Float = 0f
    var isVisible = true
    open var health: Int = 3 // Ajouté : points de vie de l'invader
    open var speed = 1f



    open fun getCollisionRect(): RectF {
        return RectF(x, y, x + invadersBitmap.width, y + invadersBitmap.height)
    }
    open fun takeDamage(damage: Int) {
        health -= damage
        if (health <= 0) {
            isVisible = false
            // L'invader est détruit s'il n'a plus de points de vie
        }
    }
    override fun update() {
        // Logique de déplacement des invaders
    }


}
