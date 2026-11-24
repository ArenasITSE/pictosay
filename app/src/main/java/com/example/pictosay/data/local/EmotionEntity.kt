
package com.example.pictosay.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emotions")
data class EmotionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    val emoji: String,

    // Texto que reproducirá el audio
    val speechText: String,

    val description: String = "",

    val isActive: Boolean = true,

    val sortOrder: Int = 0,

    val createdAt: Long = System.currentTimeMillis()
)
