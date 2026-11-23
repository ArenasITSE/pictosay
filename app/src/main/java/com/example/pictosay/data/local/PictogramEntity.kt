
package com.example.pictosay.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pictograms")
data class PictogramEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val emoji: String,
    val categoryId: Int,

    val speechText: String,
    val description: String = "",
    val isActive: Boolean = true
)
