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


    private lateinit var invader: Invader
    private lateinit var invaderRocket: InvaderRocket
    private lateinit var invaderSpeedy: InvaderSpeedy
    private lateinit var invaderTanky: InvaderTanky
    private lateinit var rocket: Rocket
    private lateinit var spaceship: Spaceship
    // Liste pour stocker les roquettes
    var rockets = mutableListOf<Rocket>()
    var invaders = mutableListOf<Invader>()
    var invaderType = 0
    var score = 0


    init {
        resetGame()
        createInvaders()
    }


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


    fun update() {
        // Mise à jour de la position des roquettes
        rockets.forEach { it.update() }
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastSpawnTime >= SPAWN_DELAY) {
            createInvaders()
            lastSpawnTime = currentTime
        }

        // Ici, vous pourriez aussi ajouter de la logique pour vérifier les collisions ou autres aspects du jeu

        // Supprimez les roquettes qui sont sorties de l'écran
        rockets.removeAll { it.y + it.bitmap.height < 0 }
        invaders.removeIf { !it.isVisible }
        checkCollisions()
        updateInvaders()

        // Demander à la vue de se redessiner
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



    init {
        handler.post(runnable)

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
        for (rocket in rockets) {
            rocket.bitmap?.let { rocketBitmap ->
                canvas.drawBitmap(rocketBitmap, rocket.x, rocket.y, null)
            }
        }
        invaders.forEach { invader ->
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

    private var invaderSpeed = 1f
    private var direction = 1 // 1 pour droite, -1 pour gauche
    // Autres propriétés et initialisations...
    private var isGameOver = false




    companion object {
        const val NUM_ROWS = 5 // Lignes d'invaders
        const val NUM_COLS = 6 // Colonnes d'invaders
        const val spacing: Float = 1f // Espacement entre chaque invader
        const val SPAWN_DELAY = 15000L // 5000 ms = 5 secondes
        const val SCORE_PER_INVADER = 500
    }
    fun invaderEliminated() {
        score += SCORE_PER_INVADER // Pour redessiner la vue et afficher le score mis à jour
        this.postInvalidate()  // Pour redessiner la vue et afficher le score mis à jour
    }


    fun createInvaders() {
        for (row in 0 until NUM_ROWS) {
            for (col in 0 until NUM_COLS) {
                val invader = when (invaderType % 3) {
                    0 -> InvaderRocket(context, this.width, this.height,rockets)
                    1 -> InvaderTanky(context, this.width, this.height)
                    else -> InvaderSpeedy(context, this.width, this.height)
                }
                invader.apply {
                    x = col * (invader.invadersBitmap.width/2 + spacing)
                    y = row * (invader.invadersBitmap.height/2 + spacing)
                    invaders.add(this)
                }
            }
        }
        invaderType++ // Incrémenter le compteur après avoir créé un groupe complet d'envahisseurs
    }

    fun launchRocket() {
        // Lancer une nouvelle roquette depuis la position actuelle du vaisseau
        val newRocket = Rocket(rocketBitmap, spaceship.x , spaceship.y, )
        rockets.add(newRocket)
    }

    // Méthode pour mettre à jour la position des roquettes et gérer la logique de jeu

    fun updateInvaders() {
        attemptToShoot()
        var edgeReached = false
        invaders.forEach { invader ->
            invader.x += invaderSpeed * direction

            // Vérifier si un invader atteint le bord de l'écran
            if (invader.x > this.width - invader.invadersBitmap.width || invader.x < 0) {
                edgeReached = true
            }
        }
        if (edgeReached) {
            direction *= -1 // Changer de direction
            invaders.forEach { invader ->
                invader.y += invader.invadersBitmap.height/10 // Descendre d'une 'ligne'
            }
        }
    }

    private fun attemptToShoot() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastShotTime >= shootingInterval && random.nextInt(100) < 5) { // 5% chance to shoot each interval
            shoot()
            lastShotTime = currentTime
        }
        rockets.forEach { it.update() }
        rockets.removeAll { !it.isVisible }
    }

    fun shoot() {
        invaders.forEach { invader ->
            val newRocket = Rocket(rocketBitmap, invader.x + invadersBitmap.width / 2 - rocketBitmap.width / 2, invader.y)
            rockets.add(newRocket)
        }
    }
    fun checkCollisions() {
        val iterator = invaders.iterator()
        val spaceshipRect = spaceship.getCollisionRect()

        invaders.forEach { invader ->
            if (invader.isVisible) {

                val invaderRect = invader.getCollisionRect()
                if (RectF.intersects(spaceshipRect, invaderRect)) {
                    // Collision détectée entre le vaisseau et un invader
                    spaceship.takeDamage(1) // Supposons que chaque collision inflige 1 point de dégât au vaisseau
                    invader.isVisible = false
                    invaderEliminated()
                    if (!isGameOver && spaceship.health <= 0) {
                        isGameOver = true
                        endGame()
                    } // Optionnel: supprimer l'invader ou gérer autrement la collision
                }
            }
        }
        while (iterator.hasNext()) {
            val invader = iterator.next()
            if (invader.isVisible) {
                val invaderRect = invader.getCollisionRect()

                rockets.forEach { rocket ->
                    val rocketRect = rocket.getCollisionRect()
                    if (RectF.intersects(invaderRect, rocketRect) && rocket.isVisible) {
                        invader.takeDamage(1) // Supposons que chaque roquette inflige 1 point de dégât
                        if (!invader.isVisible) {
                            invaderEliminated()
                        }
                        rocket.isVisible = false // La roquette est consommée par la collision
                    }
                }
            }
        }
        // Supprimez les roquettes marquées comme non visibles
        rockets.removeIf { !it.isVisible }
    }
    private fun endGame() {
        val intent = Intent(context, GameOverActivity::class.java)
        context.startActivity(intent)
    }


    fun resetGame() {
        // Initialiser le vaisseau au centre bas de l'écran
        spaceship = Spaceship(spaceshipBitmap, this.width / 2f, this.height - 150f, rockets, invaders)
        invalidate()
    }
}

