package com.example.gameinvaders

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF


// Game.kt
class Game(private val view: GameView, private val context: Context){
    val spaceshipBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.spaceship_image_background)
    val rocketBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.rocket_image_background)
    // Initialiser le vaisseau spatial avec le bitmap
    var spaceship = Spaceship(spaceshipBitmap, view.width / 2f, view.height - 150f)
    private var invaderSpeed = 1f
    private var direction = 1 // 1 pour droite, -1 pour gauche
    // Autres propriétés et initialisations...
    private var isGameOver = false
    var score = 0
        private set


    // Liste pour stocker les roquettes
    val rockets = mutableListOf<Rocket>()
    val invaders = mutableListOf<Invader>()
    private var lastSpawnTime = System.currentTimeMillis()

    private fun spawnInvaders() {
        createInvaders()
    }


    companion object {
        const val NUM_ROWS = 5 // Lignes d'invaders
        const val NUM_COLS = 6 // Colonnes d'invaders
        const val spacing: Float = 1f // Espacement entre chaque invader
        const val SPAWN_DELAY = 15000L // 5000 ms = 5 secondes
        const val SCORE_PER_INVADER = 500
    }
    fun invaderEliminated() {
        score += SCORE_PER_INVADER
        view.postInvalidate()  // Pour redessiner la vue et afficher le score mis à jour
    }
    private fun createInvaders() {
        for (row in 0 until NUM_ROWS) {
            for (col in 0 until NUM_COLS) {
                invaders.add(Invader(context, view.width, view.height).apply {
                    x = col * (invadersBitmap.width/2 + spacing)
                    y = row * (invadersBitmap.height/2 + spacing)
                })
            }
        }
    }

    fun launchRocket() {
        // Lancer une nouvelle roquette depuis la position actuelle du vaisseau
        val newRocket = Rocket(rocketBitmap, spaceship.x , spaceship.y, )
        rockets.add(newRocket)
    }

    // Méthode pour mettre à jour la position des roquettes et gérer la logique de jeu
    fun update() {
        // Mise à jour de la position des roquettes
        rockets.forEach { it.update() }
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastSpawnTime >= SPAWN_DELAY) {
            spawnInvaders()
            lastSpawnTime = currentTime
        }

        // Ici, vous pourriez aussi ajouter de la logique pour vérifier les collisions ou autres aspects du jeu

        // Supprimez les roquettes qui sont sorties de l'écran
        rockets.removeAll { it.y + it.bitmap.height < 0 }
        invaders.removeIf { !it.isVisible }
        checkCollisions()
        updateInvaders()

        // Demander à la vue de se redessiner
        view.invalidate()
    }
    private fun updateInvaders() {
        var edgeReached = false
        invaders.forEach { invader ->
            invader.x += invaderSpeed * direction

            // Vérifier si un invader atteint le bord de l'écran
            if (invader.x > view.width - invader.invadersBitmap.width || invader.x < 0) {
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

    init {
        resetGame()
        createInvaders()
    }

    fun resetGame() {
        // Initialiser le vaisseau au centre bas de l'écran
        spaceship = Spaceship(spaceshipBitmap, view.width / 2f, view.height - 150f)
    }
}
