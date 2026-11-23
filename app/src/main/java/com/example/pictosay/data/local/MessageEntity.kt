
package com.example.pictosay.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val content: String,

    // PICTOGRAM o TEXT
    val creationType: String,

    val createdAt: Long = System.currentTimeMillis()
)
