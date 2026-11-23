
package com.example.pictosay.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import androidx.room.Transaction
// ========================================
// MODELO DE CONSULTA DE FAVORITOS
// ========================================

data class FavoriteMessageItem(
    val favoriteId: Int,
    val messageId: Int,
    val emoji: String,
    val content: String
)

@Dao
interface PicToSayDao {

    // ========================================
    // CATEGORÍAS / AMBIENTES
    // ========================================

    @Query("SELECT * FROM categories ORDER BY sortOrder ASC")
    fun observeCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories ORDER BY sortOrder ASC")
    suspend fun getCategories(): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: CategoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategories(categories: List<CategoryEntity>)


    // ========================================
    // PERFIL DEL NIÑO
    // ========================================

    @Query("SELECT * FROM child_profiles LIMIT 1")
    fun observeChildProfile(): Flow<ChildProfileEntity?>

    @Query("SELECT * FROM child_profiles LIMIT 1")
    suspend fun getChildProfile(): ChildProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveChildProfile(profile: ChildProfileEntity)

    @Query("DELETE FROM child_profiles")
    suspend fun deleteChildProfile()


    // ========================================
    // PICTOGRAMAS
    // ========================================

    @Query("""
        SELECT * FROM pictograms
        WHERE isActive = 1
        ORDER BY name ASC
    """)
    fun observePictograms(): Flow<List<PictogramEntity>>

    @Query("""
        SELECT * FROM pictograms
        WHERE categoryId = :categoryId AND isActive = 1
        ORDER BY name ASC
    """)
    fun observePictogramsByCategory(
        categoryId: Int
    ): Flow<List<PictogramEntity>>

    @Query("SELECT * FROM pictograms WHERE id = :id LIMIT 1")
    suspend fun getPictogramById(id: Int): PictogramEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPictogram(pictogram: PictogramEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPictograms(pictograms: List<PictogramEntity>)

    @Update
    suspend fun updatePictogram(pictogram: PictogramEntity)

    @Query("DELETE FROM pictograms WHERE id = :id")
    suspend fun deletePictogram(id: Int)

    @Query("SELECT COUNT(*) FROM pictograms")
    suspend fun getPictogramCount(): Int


    // ========================================
    // MENSAJES
    // ========================================

    @Query("SELECT * FROM messages ORDER BY createdAt DESC")
    fun observeMessages(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE id = :id LIMIT 1")
    suspend fun getMessageById(id: Int): MessageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity): Long

    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun deleteMessage(id: Int)


    // ========================================
    // FAVORITOS
    // ========================================

    @Query("SELECT * FROM favorites ORDER BY createdAt DESC")
    fun observeFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favorite: FavoriteEntity): Long

    @Query("""
        DELETE FROM favorites
        WHERE targetType = :targetType AND targetId = :targetId
    """)
    suspend fun removeFavorite(
        targetType: String,
        targetId: Int
    )

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM favorites
            WHERE targetType = :targetType AND targetId = :targetId
        )
    """)
    suspend fun isFavorite(
        targetType: String,
        targetId: Int
    ): Boolean

    // Consultar favoritos personalizados junto con su mensaje y emoji
    @Query("""
        SELECT
            f.id AS favoriteId,
            m.id AS messageId,
            m.title AS emoji,
            m.content AS content
        FROM favorites AS f
        INNER JOIN messages AS m
            ON f.targetId = m.id
        WHERE f.targetType = 'MESSAGE'
        ORDER BY f.createdAt DESC
    """)
    fun observeFavoriteMessages(): Flow<List<FavoriteMessageItem>>

    @Transaction
    suspend fun insertFavoriteMessage(
        emoji: String,
        content: String
    ): Long {

        val messageId = insertMessage(
            MessageEntity(
                title = emoji,
                content = content.trim(),
                creationType = "PICTOGRAM"
            )
        )

        val favoriteId = addFavorite(
            FavoriteEntity(
                targetType = "MESSAGE",
                targetId = messageId.toInt()
            )
        )

        if (favoriteId == -1L) {
            deleteMessage(messageId.toInt())
            error("No se pudo guardar el favorito.")
        }

        return messageId
    }

    // Contar favoritos personalizados
    @Query("""
        SELECT COUNT(*)
        FROM favorites
        WHERE targetType = 'MESSAGE'
    """)
    suspend fun getFavoriteMessageCount(): Int

    // Recuperar un favorito por su ID
    @Query("""
        SELECT * FROM favorites
        WHERE targetType = 'MESSAGE'
        AND targetId = :messageId
        LIMIT 1
    """)
    suspend fun getFavoriteMessageById(
        messageId: Int
    ): FavoriteEntity?


    // ========================================
    // TAREAS
    // ========================================

    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun observeTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    suspend fun getTaskById(id: Int): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: Int)

    @Query("""
        UPDATE tasks
        SET isCompleted = 1, completedAt = :completedAt
        WHERE id = :taskId AND isCompleted = 0
    """)
    suspend fun completeTask(
        taskId: Int,
        completedAt: Long = System.currentTimeMillis()
    ): Int

    @Query("""
        SELECT COALESCE(SUM(starsReward), 0)
        FROM tasks
        WHERE isCompleted = 1
    """)
    fun observeTotalStars(): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM tasks
        WHERE isCompleted = 1
    """)
    fun observeCompletedTasks(): Flow<Int>

}
