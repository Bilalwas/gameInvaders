package com.example.gameinvaders

// GameView.kt
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import kotlin.math.max
import kotlin.math.min

class GameView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    lateinit var game: Game
    private var moveDirection = 0f // Direction et amplitude du mouvement
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            // Mise à jour du vaisseau spatial si nécessaire
            if (moveDirection != 0f) {
                val newPos = game.spaceship.x + (game.spaceship.speed * moveDirection)
                val bitmapWidth = game.spaceship.bitmap.width.toFloat()

                // Empêche le vaisseau de sortir de l'écran à gauche et à droite
                game.spaceship.x = max(0f, min(width - bitmapWidth, newPos))
            }

            game.update()

            invalidate()

            // Planifier la prochaine mise à jour
            handler.postDelayed(this, 16)
        }
    }
    private val updateRunnable = object : Runnable {
        override fun run() {
            game.update() // Met à jour la position de toutes les roquettes
            invalidate() // Redessine la vue
            handler.postDelayed(this, 16) // Planifie la prochaine mise à jour
        }
    }

    override fun onSizeChanged(newWidth: Int, newHeight: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight)
        game = Game(this, context)
        game.spaceship.x = (newWidth - game.spaceship.bitmap.width) / 2f
        game.spaceship.y = newHeight - game.spaceship.bitmap.height - 100f
    }

    private fun addLaunchRocketButton() {
        val launchRocketButton = Button(context).apply {
            text = "Launch Rocket"
            // Configurez le bouton ici
        }

        // Configurez ici pour ajouter le bouton à un ViewGroup si nécessaire
        // Par exemple:
        // (parent as? ViewGroup)?.addView(launchRocketButton)

        launchRocketButton.setOnClickListener {
            game.launchRocket()
        }
    }

    init {
        game = Game(this, context)
        handler.post(runnable)
        handler.post(updateRunnable)
        addLaunchRocketButton()
    }


    @SuppressLint("DrawAllocation", "SuspiciousIndentation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        game.spaceship.bitmap?.let { spaceshipBitmap ->
            canvas.drawBitmap(spaceshipBitmap, game.spaceship.x, game.spaceship.y, null)
        }
        for (rocket in game.rockets) {
            rocket.bitmap?.let { rocketBitmap ->
                canvas.drawBitmap(rocketBitmap, rocket.x, rocket.y, null)
            }
        }
        game.invaders.forEach { invader ->
            if (invader.isVisible) {
                canvas.drawBitmap(invader.invadersBitmap, invader.x, invader.y, null)
            }

        }
        val scorePaint = Paint().apply {
            color = Color.BLUE
            textSize = 60f
            typeface = Typeface.DEFAULT_BOLD
        }

        // Affiche le score actuel en haut à gauche de l'écran
        canvas.drawText("Score: ${game.score}", 50f, 100f, scorePaint)
    }

        @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val activeZoneHeight = height * 0.75f
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
            // Vérifiez si le toucher est dans la zone active.
                if (event.y > activeZoneHeight) {
                    moveDirection = if (event.x < game.spaceship.x) -1f else 1f
                    handler.removeCallbacks(runnable)
                    handler.post(runnable)
                }
            }

            MotionEvent.ACTION_UP -> {
                moveDirection = 0f
                handler.removeCallbacks(runnable)
            }
        }
        return true
    }
}
