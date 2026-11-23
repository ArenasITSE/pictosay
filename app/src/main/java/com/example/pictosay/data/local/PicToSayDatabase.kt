
package com.example.pictosay.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        CategoryEntity::class,
        ChildProfileEntity::class,
        PictogramEntity::class,
        MessageEntity::class,
        FavoriteEntity::class,
        TaskEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PicToSayDatabase : RoomDatabase() {

    abstract fun picToSayDao(): PicToSayDao

    companion object {

        @Volatile
        private var INSTANCE: PicToSayDatabase? = null

        fun getDatabase(context: Context): PicToSayDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PicToSayDatabase::class.java,
                    "pictosay_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}
