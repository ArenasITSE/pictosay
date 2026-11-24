package com.example.pictosay.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        CategoryEntity::class,
        ChildProfileEntity::class,
        PictogramEntity::class,
        MessageEntity::class,
        FavoriteEntity::class,
        TaskEntity::class,
        EmotionEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class PicToSayDatabase : RoomDatabase() {

    abstract fun picToSayDao(): PicToSayDao

    companion object {

        @Volatile
        private var INSTANCE: PicToSayDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS emotions (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        emoji TEXT NOT NULL,
                        speechText TEXT NOT NULL,
                        description TEXT NOT NULL,
                        isActive INTEGER NOT NULL,
                        sortOrder INTEGER NOT NULL,
                        createdAt INTEGER NOT NULL
                    )
                """.trimIndent())
            }
        }

        fun getDatabase(context: Context): PicToSayDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PicToSayDatabase::class.java,
                    "pictosay_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
