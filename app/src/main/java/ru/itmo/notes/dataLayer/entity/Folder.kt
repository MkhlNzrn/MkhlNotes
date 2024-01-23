package ru.itmo.notes.dataLayer.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "folders")
class Folder : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
    @ColumnInfo(name = "folder_name")
    var folderName: String = ""
}