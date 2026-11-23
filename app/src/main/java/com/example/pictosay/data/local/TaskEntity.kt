
package com.example.pictosay.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val description: String = "",
    val emoji: String = "⭐",

    val starsReward: Int = 1,
    val isCompleted: Boolean = false,

    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)
