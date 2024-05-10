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
    val spaceshipBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.spaceship_image_background)
    val spaceshiprocketBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.spaceship_rocket_image_background)
    val invaderrocketBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.invader_rocket_image_background)
    val invaderBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.invader_image_background)
    val healthBonusBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.health_bonus_image_background)
    val damageBonusBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.damage_bonus_image_background)



    private lateinit var invader: Invader
    private lateinit var invaderRocket: RocketInvader
    private lateinit var invaderSpeedy: SpeedyInvader
    private lateinit var invaderTanky: TankyInvader
    private lateinit var rocket: Rocket
    lateinit var spaceship: Spaceship
    val score = Observable(0)
    var Irockets = mutableListOf<InvaderRocket>()
    var Srockets = mutableListOf<SpaceshipRocket>()
    private val bonuses = mutableListOf<Bonus>()
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
    private val bonusRunnable = object : Runnable {
        override fun run() {
            if (!isGamePaused && !isGameOver) {
                createBonus() // Créer un bonus
                handler.postDelayed(this, 10000) // Générer un nouveau bonus toutes les 10 secondes
            }
        }
    }
    init {
        resetGame()
        handler.post(runnable)
        handler.post(bonusRunnable)
    }
    fun update() {
        Srockets.forEach { it.update() }
        Irockets.forEach { it.update() }
        Srockets.removeIf { !it.isVisible }
        Irockets.removeIf { !it.isVisible }
        bonuses.forEach { it.update() }
        bonuses.removeIf { !it.isVisible }
        spaceship.checkCollisions(bonuses)
        if (!isGameOver && spaceship.health.get() <= 0) {
            isGameOver = true
            endGame()
        }
        invaders.checkCollisions(Srockets, score)
        invaders.updateInvaders(this.width, invaderrocketBitmap)
        invaders.removeDestroyedInvaders()
        if (invaders.allInvadersDestroyed()) {
            invaders.resetInvaders()
        }
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
    private fun createBonus() {
        val random = Random()
        val x = random.nextInt(this.width)
        val bonusType = random.nextInt(2)

        val newBonus = when (bonusType) {
            0 -> HealthBonus(healthBonusBitmap, x.toFloat(), 0f)
            else -> DamageBonus(damageBonusBitmap, x.toFloat(), 0f)
        }

        bonuses.add(newBonus)
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
        spaceship.bitmap?.let { paceshipBitmap ->
            canvas.drawBitmap(spaceshipBitmap, spaceship.x, spaceship.y, null)
        }
        for (rocket in Irockets) {
            rocket.bitmap?.let { nvaderrocketBitmap ->
                canvas.drawBitmap(invaderrocketBitmap, rocket.x, rocket.y, null)
            }
        }
        for (rocket in Srockets) {
            rocket.bitmap?.let { paceshiprocketBitmap ->
                canvas.drawBitmap(spaceshiprocketBitmap, rocket.x, rocket.y, null)
            }
        }
        invaders.invadersList.forEach { invader ->
            if (invader.isVisible) {
                canvas.drawBitmap(invader.invadersBitmap, invader.x, invader.y, null)
            }
        }
        bonuses.forEach { bonus ->
            if (bonus is HealthBonus) {
                bonus.bitmap.let { bonusBitmap ->
                    canvas.drawBitmap(healthBonusBitmap, bonus.x, bonus.y, null)
                }
            } else {
                bonus.bitmap.let { bonusBitmap ->
                    canvas.drawBitmap(damageBonusBitmap, bonus.x, bonus.y, null)
                }
            }
        }
        val scorePaint = Paint().apply {
            color = Color.BLUE
            textSize = 60f
            typeface = Typeface.DEFAULT_BOLD
        }
        canvas.drawText("Score: ${score.get()}", 50f, 100f, scorePaint)
    }
        @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val activeZoneHeight = height * 0.75f
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
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