
package com.example.pictosay.data.repository

import com.example.pictosay.data.local.CategoryEntity
import com.example.pictosay.data.local.ChildProfileEntity
import com.example.pictosay.data.local.FavoriteEntity
import com.example.pictosay.data.local.FavoriteMessageItem
import com.example.pictosay.data.local.MessageEntity
import com.example.pictosay.data.local.PicToSayDao
import com.example.pictosay.data.local.PictogramEntity
import com.example.pictosay.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow

class PicToSayRepository(
    private val dao: PicToSayDao
) {

    // ========================================
    // AMBIENTES
    // ========================================

    val categories: Flow<List<CategoryEntity>> =
        dao.observeCategories()


    // ========================================
    // PERFIL DEL NIÑO
    // ========================================

    val childProfile: Flow<ChildProfileEntity?> =
        dao.observeChildProfile()

    suspend fun saveChildProfile(profile: ChildProfileEntity) {
        dao.saveChildProfile(profile)
    }

    suspend fun getChildProfile(): ChildProfileEntity? {
        return dao.getChildProfile()
    }


    // ========================================
    // PICTOGRAMAS
    // ========================================

    val pictograms: Flow<List<PictogramEntity>> =
        dao.observePictograms()

    fun getPictogramsByCategory(
        categoryId: Int
    ): Flow<List<PictogramEntity>> {
        return dao.observePictogramsByCategory(categoryId)
    }

    suspend fun addPictogram(pictogram: PictogramEntity) {
        dao.insertPictogram(pictogram)
    }

    suspend fun deletePictogram(id: Int) {
        dao.deletePictogram(id)
    }


    // ========================================
    // MENSAJES
    // ========================================

    val messages: Flow<List<MessageEntity>> =
        dao.observeMessages()

    suspend fun saveMessage(message: MessageEntity): Long {
        return dao.insertMessage(message)
    }

    suspend fun deleteMessage(id: Int) {
        dao.deleteMessage(id)
    }


    // ========================================
    // FAVORITOS GENERALES
    // ========================================

    val favorites: Flow<List<FavoriteEntity>> =
        dao.observeFavorites()

    suspend fun addFavorite(
        targetType: String,
        targetId: Int
    ) {
        dao.addFavorite(
            FavoriteEntity(
                targetType = targetType,
                targetId = targetId
            )
        )
    }

    suspend fun removeFavorite(
        targetType: String,
        targetId: Int
    ) {
        dao.removeFavorite(targetType, targetId)
    }

    suspend fun isFavorite(
        targetType: String,
        targetId: Int
    ): Boolean {
        return dao.isFavorite(targetType, targetId)
    }


    // ========================================
    // FAVORITOS PERSONALIZADOS DEL TUTOR
    // ========================================

    /**
     * Lista de favoritos personalizados.
     * Recupera el emoji y el mensaje desde Room.
     */
    val favoriteMessages: Flow<List<FavoriteMessageItem>> =
        dao.observeFavoriteMessages()


    /**
     * Guarda un mensaje personalizado del tutor.
     *
     * El emoji se almacena en title.
     * El mensaje se almacena en content.
     *
     * El DAO realiza ambas inserciones dentro
     * de una transacción.
     */
    suspend fun saveFavoriteMessage(
        emoji: String,
        message: String
    ): Long {

        require(emoji.isNotBlank()) {
            "Debes seleccionar un emoji."
        }

        require(message.isNotBlank()) {
            "Debes escribir un mensaje."
        }

        return dao.insertFavoriteMessage(
            emoji = emoji.trim(),
            content = message.trim()
        )
    }


    /**
     * Elimina un favorito personalizado.
     *
     * Primero elimina su relación con favoritos
     * y después elimina el mensaje.
     */
    suspend fun removeFavoriteMessage(messageId: Int) {

        dao.removeFavorite(
            targetType = "MESSAGE",
            targetId = messageId
        )

        dao.deleteMessage(messageId)
    }


    /**
     * Devuelve la cantidad de favoritos personalizados.
     */
    suspend fun getFavoriteMessageCount(): Int {
        return dao.getFavoriteMessageCount()
    }


    /**
     * Inserta los favoritos iniciales si todavía
     * no existen favoritos personalizados.
     */
    suspend fun initializeDefaultFavorites() {

        if (dao.getFavoriteMessageCount() == 0) {

            val defaults = listOf(
                "💧" to "Quiero agua",
                "🍎" to "Tengo hambre",
                "⚽" to "Quiero jugar",
                "🙋" to "Necesito ayuda",
                "😄" to "Estoy feliz"
            )

            defaults.forEach { (emoji, message) ->

                saveFavoriteMessage(
                    emoji = emoji,
                    message = message
                )
            }
        }
    }


    // ========================================
    // TAREAS Y ESTRELLAS
    // ========================================

    val tasks: Flow<List<TaskEntity>> =
        dao.observeTasks()

    val totalStars: Flow<Int> =
        dao.observeTotalStars()

    val completedTasks: Flow<Int> =
        dao.observeCompletedTasks()

    suspend fun addTask(task: TaskEntity): Long {
        return dao.insertTask(task)
    }

    suspend fun updateTask(task: TaskEntity) {
        dao.updateTask(task)
    }

    suspend fun deleteTask(id: Int) {
        dao.deleteTask(id)
    }


    /**
     * Marca una tarea como completada.
     *
     * Devuelve las estrellas ganadas.
     * Si ya estaba completada, devuelve 0.
     */
    suspend fun completeTask(taskId: Int): Int {

        val task = dao.getTaskById(taskId) ?: return 0

        if (task.isCompleted) {
            return 0
        }

        val updatedRows = dao.completeTask(taskId)

        return if (updatedRows > 0) {
            task.starsReward
        } else {
            0
        }
    }


    // ========================================
    // DATOS INICIALES
    // ========================================

    suspend fun initializeDefaultData() {

        // ------------------------------------
        // CATEGORÍAS / AMBIENTES
        // ------------------------------------

        val defaultCategories = listOf(

            CategoryEntity(
                id = 1,
                name = "Mi cuarto",
                emoji = "🛏️",
                description = "Objetos de mi habitación",
                sortOrder = 1
            ),

            CategoryEntity(
                id = 2,
                name = "El baño",
                emoji = "🚿",
                description = "Objetos para mi higiene",
                sortOrder = 2
            ),

            CategoryEntity(
                id = 3,
                name = "La cocina",
                emoji = "🍳",
                description = "Alimentos y objetos de cocina",
                sortOrder = 3
            ),

            CategoryEntity(
                id = 4,
                name = "Mi casa y patio",
                emoji = "🏡",
                description = "Personas, animales y objetos de casa",
                sortOrder = 4
            ),

            CategoryEntity(
                id = 5,
                name = "Supermercado",
                emoji = "🛒",
                description = "Productos que puedo comprar",
                sortOrder = 5
            ),

            CategoryEntity(
                id = 6,
                name = "Mi escuela",
                emoji = "🏫",
                description = "Objetos y actividades escolares",
                sortOrder = 6
            )
        )

        dao.insertCategories(defaultCategories)


        // ------------------------------------
        // PICTOGRAMAS INICIALES
        // ------------------------------------

        // Evita duplicar los pictogramas
        // al abrir nuevamente la aplicación.

        if (dao.getPictogramCount() == 0) {

            val defaultPictograms = listOf(

                // ====================================
                // MI CUARTO
                // ====================================

                PictogramEntity(
                    name = "Cama",
                    emoji = "🛏️",
                    categoryId = 1,
                    speechText = "Cama"
                ),

                PictogramEntity(
                    name = "Almohada",
                    emoji = "🛌",
                    categoryId = 1,
                    speechText = "Almohada"
                ),

                PictogramEntity(
                    name = "Juguete",
                    emoji = "🧸",
                    categoryId = 1,
                    speechText = "Juguete"
                ),

                PictogramEntity(
                    name = "Ropa",
                    emoji = "👕",
                    categoryId = 1,
                    speechText = "Ropa"
                ),


                // ====================================
                // EL BAÑO
                // ====================================

                PictogramEntity(
                    name = "Baño",
                    emoji = "🚽",
                    categoryId = 2,
                    speechText = "Baño"
                ),

                PictogramEntity(
                    name = "Regadera",
                    emoji = "🚿",
                    categoryId = 2,
                    speechText = "Regadera"
                ),

                PictogramEntity(
                    name = "Jabón",
                    emoji = "🧼",
                    categoryId = 2,
                    speechText = "Jabón"
                ),

                PictogramEntity(
                    name = "Cepillo de dientes",
                    emoji = "🪥",
                    categoryId = 2,
                    speechText = "Cepillo de dientes"
                ),


                // ====================================
                // LA COCINA
                // ====================================

                PictogramEntity(
                    name = "Agua",
                    emoji = "💧",
                    categoryId = 3,
                    speechText = "Agua"
                ),

                PictogramEntity(
                    name = "Manzana",
                    emoji = "🍎",
                    categoryId = 3,
                    speechText = "Manzana"
                ),

                PictogramEntity(
                    name = "Pan",
                    emoji = "🍞",
                    categoryId = 3,
                    speechText = "Pan"
                ),

                PictogramEntity(
                    name = "Leche",
                    emoji = "🥛",
                    categoryId = 3,
                    speechText = "Leche"
                ),


                // ====================================
                // MI CASA Y PATIO
                // ====================================

                PictogramEntity(
                    name = "Perro",
                    emoji = "🐶",
                    categoryId = 4,
                    speechText = "Perro"
                ),

                PictogramEntity(
                    name = "Gato",
                    emoji = "🐱",
                    categoryId = 4,
                    speechText = "Gato"
                ),

                PictogramEntity(
                    name = "Carro",
                    emoji = "🚗",
                    categoryId = 4,
                    speechText = "Carro"
                ),

                PictogramEntity(
                    name = "Pelota",
                    emoji = "⚽",
                    categoryId = 4,
                    speechText = "Pelota"
                ),


                // ====================================
                // SUPERMERCADO
                // ====================================

                PictogramEntity(
                    name = "Plátano",
                    emoji = "🍌",
                    categoryId = 5,
                    speechText = "Plátano"
                ),

                PictogramEntity(
                    name = "Galletas",
                    emoji = "🍪",
                    categoryId = 5,
                    speechText = "Galletas"
                ),

                PictogramEntity(
                    name = "Jugo",
                    emoji = "🧃",
                    categoryId = 5,
                    speechText = "Jugo"
                ),

                PictogramEntity(
                    name = "Carrito",
                    emoji = "🛒",
                    categoryId = 5,
                    speechText = "Carrito"
                ),


                // ====================================
                // MI ESCUELA
                // ====================================

                PictogramEntity(
                    name = "Mochila",
                    emoji = "🎒",
                    categoryId = 6,
                    speechText = "Mochila"
                ),

                PictogramEntity(
                    name = "Libro",
                    emoji = "📚",
                    categoryId = 6,
                    speechText = "Libro"
                ),

                PictogramEntity(
                    name = "Lápiz",
                    emoji = "✏️",
                    categoryId = 6,
                    speechText = "Lápiz"
                ),

                PictogramEntity(
                    name = "Maestra",
                    emoji = "👩‍🏫",
                    categoryId = 6,
                    speechText = "Maestra"
                )
            )

            dao.insertPictograms(defaultPictograms)
        }
    }
}
