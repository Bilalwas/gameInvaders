package com.example.gameinvaders
import android.content.Context
import android.graphics.Bitmap
import android.util.Log

enum class InvaderType {
    ROCKET, SPEEDY, TANKY
}

object InvaderFactory {
    fun createInvader(
        type: InvaderType,
        context: Context,
        screenWidth: Int,
        screenHeight: Int,
        rockets: MutableList<InvaderRocket> = mutableListOf()
    ): Invader {
        return when (type) {
            InvaderType.ROCKET -> RocketInvader(context, screenWidth, screenHeight, rockets)
            InvaderType.SPEEDY -> SpeedyInvader(context, screenWidth, screenHeight)
            InvaderType.TANKY -> TankyInvader(context, screenWidth, screenHeight)
        }
    }
}
