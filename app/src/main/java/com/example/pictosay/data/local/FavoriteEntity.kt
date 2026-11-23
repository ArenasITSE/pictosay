
package com.example.pictosay.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorites",
    indices = [
        Index(
            value = ["targetType", "targetId"],
            unique = true
        )
    ]
)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // PICTOGRAM o MESSAGE
    val targetType: String,

    val targetId: Int,

    val createdAt: Long = System.currentTimeMillis()
)
