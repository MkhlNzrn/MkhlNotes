package ru.itmo.notes.dataLayer.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.itmo.notes.dataLayer.entity.Note

@Dao
interface NoteDao {

    @Query("select * from notes order by id desc")
    fun getAll(): List<Note>

    @Query("select * from notes where folder_id=:folderId and is_deleted = 0 order by id desc")
    fun getLiveByFolderId(folderId: Long): List<Note>

    @Query("select * from notes where folder_id=:folderId and is_deleted = 1 order by id desc")
    fun getDelByFolderId(folderId: Long): List<Note>

    @Query("select * from notes where is_deleted = 1 order by id desc")
    fun getAllDeleted(): List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Query("update notes set note_title=:noteTitle, note_text=:noteText where id=:id")
    fun update(id: Long, noteTitle: String, noteText: String)

    @Query("update notes set is_deleted = 1 where id=:id")
    fun moveToGarbage(id: Long)

    @Query("update notes set is_deleted = 0 where id=:id")
    fun restore(id: Long)

    @Delete
    fun delete(note: Note)
}