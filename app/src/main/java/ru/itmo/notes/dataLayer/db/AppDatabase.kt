package ru.itmo.notes.dataLayer.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.itmo.notes.dataLayer.dao.FolderDao
import ru.itmo.notes.dataLayer.dao.NoteDao
import ru.itmo.notes.dataLayer.entity.Folder
import ru.itmo.notes.dataLayer.entity.Note

@Database(entities = [Note::class, Folder::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun folderDao():FolderDao
    abstract fun noteDao():NoteDao

    companion object {
        private var database: AppDatabase? = null
        private const val DATABASE_NAME = "NotesApp"

        fun getInstance(context: Context) : AppDatabase{
            return database?: synchronized(this){
                database?:buildDatabase(context).also { database = it }
            }
        }

        private fun buildDatabase(context: Context) : AppDatabase{
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}