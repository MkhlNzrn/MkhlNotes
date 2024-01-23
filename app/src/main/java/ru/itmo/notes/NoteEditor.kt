package ru.itmo.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.itmo.notes.dataLayer.entity.Note

class NoteEditor: AppCompatActivity() {
    lateinit var editTextNoteTitle: EditText
    lateinit var editTextNoteText: EditText
    lateinit var note: Note
    lateinit var imageViewSave: ImageView
    var folderId: Long = 0L
    var isOldNote: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_editor)

        editTextNoteTitle = findViewById(R.id.editText_noteTitle)
        editTextNoteText = findViewById(R.id.editText_noteText)
        imageViewSave = findViewById(R.id.imageView_saveNote)
        folderId = intent.getSerializableExtra("folder_id") as Long

        try {
            note = intent.getSerializableExtra("note") as Note
            editTextNoteTitle.setText(note.noteTitle)
            editTextNoteText.setText(note.noteText)
            isOldNote = true
        } catch (e: Exception){
            e.printStackTrace()
        }

        imageViewSave.setOnClickListener{
            val noteTitle = editTextNoteTitle.text.toString()
            val noteText = editTextNoteText.text.toString()

            if(noteTitle.isEmpty()){
                Toast.makeText(this@NoteEditor, "Enter note title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!isOldNote) {
                note = Note()
            }

            note.noteTitle = noteTitle
            note.noteText = noteText
            note.folderId = folderId

            val intent = Intent()
            intent.putExtra("note", note)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}