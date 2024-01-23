package ru.itmo.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.itmo.notes.dataLayer.entity.Folder

class FolderEditor : AppCompatActivity() {
    lateinit var editText_folderName: EditText
    lateinit var folder: Folder
    lateinit var imageView_save: ImageView
    var isOldFolder: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.folder_editor)

        editText_folderName = findViewById(R.id.editText_folderName)
        imageView_save = findViewById(R.id.imageView_save)

        try {
            folder = intent.getSerializableExtra("old_folder") as Folder
            editText_folderName.setText(folder.folderName)
            isOldFolder = true
        } catch (e: Exception){
            e.printStackTrace()
        }

        imageView_save.setOnClickListener{
            val folderName = editText_folderName.text.toString()

            if(folderName.isEmpty()){
                Toast.makeText(this@FolderEditor, "Enter folder name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!isOldFolder) {
                folder = Folder()
            }

            folder.folderName = folderName

            val intent = Intent()
            intent.putExtra("folder", folder)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}