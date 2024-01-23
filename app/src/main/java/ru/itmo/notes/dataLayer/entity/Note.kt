package ru.itmo.notes.dataLayer.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "notes", foreignKeys =
    [
        ForeignKey(
            entity = Folder::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("folder_id"),
            onDelete = ForeignKey.CASCADE
        )
    ], indices = [Index(value = arrayOf("folder_id"))]
)
class Note() : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
    @ColumnInfo(name = "folder_id")
    var folderId: Long = 0L
    @ColumnInfo(name = "note_title")
    var noteTitle: String = ""
    @ColumnInfo(name = "note_text")
    var noteText: String = ""
    @ColumnInfo(name = "is_deleted")
    var isDeleted: Boolean = false;
}