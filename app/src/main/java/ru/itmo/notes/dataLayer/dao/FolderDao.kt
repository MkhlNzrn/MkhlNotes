package ru.itmo.notes.dataLayer.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import ru.itmo.notes.dataLayer.entity.Folder

@Dao
interface FolderDao {
    @Query("select * from folders order by id desc")
    fun getAll(): List<Folder>

    @Insert(onConflict = REPLACE)
    fun insert(folder: Folder)

    @Query("update folders set folder_name=:folderName where id=:id")
    fun update(id: Long, folderName: String)

    @Delete
    fun delete(folder: Folder)
}