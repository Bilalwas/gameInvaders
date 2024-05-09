package com.example.gameinvaders

// GameView.kt
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.Random
import kotlin.math.max
import kotlin.math.min

class GameView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var moveDirection = 0f // Direction et amplitude du mouvement
    private val handler = Handler(Looper.getMainLooper())
    private var backgroundBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.fond)
    var isGamePaused = false
    private var lastShotTime: Long = System.currentTimeMillis()
    private val shootingInterval = 10 * 1000  // 100 seconds
    private val random = Random()
    private var lastSpawnTime = System.currentTimeMillis()
    val spaceshipBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.spaceship_image_background)
    var invadersBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.invader_image_background)
    val rocketBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.rocket_image_background)
    val invaderBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.invader_image_background)



    private lateinit var invader: Invader
    private lateinit var invaderRocket: RocketInvader
    private lateinit var invaderSpeedy: SpeedyInvader
    private lateinit var invaderTanky: TankyInvader
    private lateinit var rocket: Rocket
    lateinit var spaceship: Spaceship
    // Liste pour stocker les roquettes
    var Irockets = mutableListOf<InvaderRocket>()
    var Srockets = mutableListOf<SpaceshipRocket>()
    var invaderType = 0
    var score = 0
    val invaders = Invaders(context, width, height, invaderBitmap, Irockets)
    private var isGameOver = false

    val runnable = object : Runnable {
        override fun run() {
            if (!isGamePaused) {
                update()
                invalidate()
                if (moveDirection != 0f) {
                    val newPos = spaceship.x + (spaceship.speed * moveDirection)
                    val bitmapWidth = spaceship.bitmap.width.toFloat()
                    spaceship.x = max(0f, min(width - bitmapWidth, newPos))
                }
            handler.postDelayed(this, 16)
            }
        }
    }
    init {
        resetGame()
        handler.post(runnable)
    }
    fun update() {
        Srockets.forEach { it.update() }
        Irockets.forEach { it.update() }
        Srockets.removeIf { !it.isVisible }
        Irockets.removeIf { !it.isVisible }
        spaceship.checkCollisions()
        if (!isGameOver && spaceship.health <= 0) {
            isGameOver = true
            endGame()
        }
        invaders.checkCollisions(Srockets)
        invaders.updateInvaders(this.width, rocketBitmap)
        invaders.removeDestroyedInvaders()
        this.invalidate()
    }
    override fun onSizeChanged(newWidth: Int, newHeight: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight)
        spaceship.x = (newWidth - spaceship.bitmap.width) / 2f
        spaceship.y = newHeight - spaceship.bitmap.height - 100f
        backgroundBitmap?.let {
            val resizedBitmap = Bitmap.createScaledBitmap(it, newWidth, newHeight, false)
            backgroundBitmap = resizedBitmap
        }
    }

    fun pauseGame() {
        isGamePaused = true // Marque le jeu comme étant en pause
        handler.removeCallbacks(runnable) // Arrête les mises à jour
    }

    fun resumeGame() {
        isGamePaused = false // Marque le jeu comme n'étant plus en pause
        handler.postDelayed(runnable, 16) // Reprend les mises à jour
    }

    @SuppressLint("DrawAllocation", "SuspiciousIndentation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        backgroundBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }
        spaceship.bitmap?.let { spaceshipBitmap ->
            canvas.drawBitmap(spaceshipBitmap, spaceship.x, spaceship.y, null)
        }
        for (rocket in Irockets) {
            rocket.bitmap?.let { rocketBitmap ->
                canvas.drawBitmap(rocketBitmap, rocket.x, rocket.y, null)
            }
        }
        for (rocket in Srockets) {
            rocket.bitmap?.let { rocketBitmap ->
                canvas.drawBitmap(rocketBitmap, rocket.x, rocket.y, null)
            }
        }
        invaders.invadersList.forEach { invader ->
            if (invader.isVisible) {
                canvas.drawBitmap(invader.invadersBitmap, invader.x, invader.y, null)
            }
        }
        val scorePaint = Paint().apply {
            color = Color.BLUE
            textSize = 60f
            typeface = Typeface.DEFAULT_BOLD
        }
        canvas.drawText("Score: ${score}", 50f, 100f, scorePaint)
    }
        @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val activeZoneHeight = height * 0.75f
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
            // Vérifiez si le toucher est dans la zone active.
                if (event.y > activeZoneHeight) {
                    moveDirection = if (event.x < spaceship.x) -1f else 1f
                }
            }
            MotionEvent.ACTION_UP -> {
                moveDirection = 0f

            }
        }
        return true
    }
    companion object {
        const val NUM_ROWS = 5 // Lignes d'invaders
        const val NUM_COLS = 6 // Colonnes d'invaders
        const val spacing: Float = 1f // Espacement entre chaque invader
        const val SPAWN_DELAY = 15000L // 5000 ms = 5 secondes
        const val SCORE_PER_INVADER = 500
    }
    private fun endGame() {
        val intent = Intent(context, GameOverActivity::class.java)
        context.startActivity(intent)
    }
    fun resetGame() {
        // Initialiser le vaisseau au centre bas de l'écran
        spaceship = Spaceship(spaceshipBitmap, this.width / 2f, this.height - 150f, Srockets, invaders, score)
        invalidate()
    }
}