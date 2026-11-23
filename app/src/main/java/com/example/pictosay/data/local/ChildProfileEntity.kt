
package com.example.pictosay.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "child_profiles")
data class ChildProfileEntity(
    @PrimaryKey
    val id: Int = 1,

    val name: String,
    val gender: String,
    val mascot: String,

    val onboardingCompleted: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
