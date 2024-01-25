package com.example.nots.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nots.entities.LibraryItem
import com.example.nots.entities.NoteItem
import com.example.nots.entities.ShopListItem
import com.example.nots.entities.ShopListNameItem

@Database(entities = [LibraryItem:: class, NoteItem:: class,
    ShopListItem:: class, ShopListNameItem::class], version = 1)
abstract class MainDatabase : RoomDatabase() {
    abstract fun getDao(): Dao

    companion object{
        @Volatile
        private var INSTANCE: MainDatabase? = null
        fun getDatabase(context: Context): MainDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDatabase:: class.java,
                    "shopping_list.db"
                ).build()
                instance
            }
        }
    }
}