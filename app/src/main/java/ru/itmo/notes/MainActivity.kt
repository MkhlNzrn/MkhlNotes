package ru.itmo.notes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.itmo.notes.adapter.FolderListAdapter
import ru.itmo.notes.adapter.NoteListAdapter
import ru.itmo.notes.dataLayer.db.AppDatabase
import ru.itmo.notes.dataLayer.entity.Folder
import ru.itmo.notes.dataLayer.entity.Note
import ru.itmo.notes.listener.FolderClickListener
import ru.itmo.notes.listener.NoteClickListener

class MainActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    lateinit var recyclerView: RecyclerView
    lateinit var folderAddButton: FloatingActionButton
    lateinit var noteAddButton: FloatingActionButton
    lateinit var garbageButton: FloatingActionButton
    lateinit var backFromFolderButton: FloatingActionButton
    lateinit var backFromGarbageButton: FloatingActionButton
    lateinit var folderListAdapter: FolderListAdapter
    lateinit var noteListAdapter: NoteListAdapter
    lateinit var database: AppDatabase;
    var folders: MutableList<Folder> = ArrayList()
    var noteList: MutableList<Note> = ArrayList()
    lateinit var selectedFolder: Folder
    lateinit var selectedNote: Note

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_home)
        folderAddButton = findViewById(R.id.folder_add_button)
        noteAddButton = findViewById(R.id.note_add_button)
        garbageButton = findViewById(R.id.garbage_button)
        backFromFolderButton = findViewById(R.id.back_from_folder_button)
        backFromGarbageButton = findViewById(R.id.back_from_garbage_button)
        database = AppDatabase.getInstance(this)

        folders = database.folderDao().getAll().toMutableList()
        updateRecyclerToFolder(folders)

        folderAddButton.setOnClickListener {
            val intent = Intent(this@MainActivity, FolderEditor::class.java)
            startActivityForResult(intent, 101)
            folderListAdapter.notifyDataSetChanged()
        }

        garbageButton.setOnClickListener {
            garbageButton.visibility = View.INVISIBLE
            backFromGarbageButton.visibility = View.VISIBLE
            noteList = database.noteDao().getDelByFolderId(selectedFolder.id).toMutableList()
            updateRecyclerToNote(noteList)
        }

        backFromGarbageButton.setOnClickListener {
            backFromGarbageButton.visibility = View.INVISIBLE
            garbageButton.visibility = View.VISIBLE
            noteList = database.noteDao().getLiveByFolderId(selectedFolder.id).toMutableList()
            updateRecyclerToNote(noteList)
        }

        backFromFolderButton.setOnClickListener {
            updateRecyclerToFolder(folders)
            garbageButton.visibility = View.INVISIBLE
            backFromFolderButton.visibility = View.INVISIBLE
            folderAddButton.visibility = View.VISIBLE
            noteAddButton.visibility = View.INVISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                val newFolder = data?.getSerializableExtra("folder") as Folder
                database.folderDao().insert(newFolder)
                folders.clear()
                folders.addAll(database.folderDao().getAll())
                folderListAdapter.notifyDataSetChanged()
            }
        } else if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                val newFolder = data?.getSerializableExtra("folder") as Folder
                database.folderDao().update(newFolder.id, newFolder.folderName)
                folders.clear()
                folders.addAll(database.folderDao().getAll())
                folderListAdapter.notifyDataSetChanged()
            }
        } else if (requestCode == 103) {
            if (resultCode == Activity.RESULT_OK) {
                val newNote = data?.getSerializableExtra("note") as Note
                database.noteDao().insert(newNote)
                noteList.clear()
                noteList.addAll(database.noteDao().getLiveByFolderId(newNote.folderId))
                noteListAdapter.notifyDataSetChanged()
            }
        } else if (requestCode == 104) {
            if (resultCode == Activity.RESULT_OK) {
                val newNote = data?.getSerializableExtra("note") as Note
                database.noteDao().update(newNote.id, newNote.noteTitle, newNote.noteText)
                noteList.clear()
                noteList.addAll(database.noteDao().getLiveByFolderId(newNote.folderId))
                noteListAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun updateRecyclerToFolder(folders: List<Folder>) {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        folderListAdapter = FolderListAdapter(this@MainActivity, folders, folderClickListener)
        recyclerView.adapter = folderListAdapter
    }

    private fun updateRecyclerToNote(noteList: List<Note>) {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        noteListAdapter = NoteListAdapter(this@MainActivity, noteList, noteClickListener)
        recyclerView.adapter = noteListAdapter
    }

    private val folderClickListener = object : FolderClickListener {
        override fun onClick(folder: Folder) {
            garbageButton.visibility = View.VISIBLE
            backFromFolderButton.visibility = View.VISIBLE
            folderAddButton.visibility = View.INVISIBLE
            noteAddButton.visibility = View.VISIBLE

            selectedFolder = folder
            noteList = database.noteDao().getLiveByFolderId(selectedFolder.id).toMutableList()
            updateRecyclerToNote(noteList)
            noteAddButton.setOnClickListener {
                val intent = Intent(this@MainActivity, NoteEditor::class.java)
                intent.putExtra("folder_id", selectedFolder.id)
                startActivityForResult(intent, 103)
                noteListAdapter.notifyDataSetChanged()
            }
        }

        override fun onLongClick(folder: Folder, cardView: CardView) {
            selectedFolder = folder
            showFolderPopUp(cardView)
        }
    }

    private val noteClickListener = object : NoteClickListener {
        override fun onClick(note: Note) {
            selectedNote = note
            val intent = Intent(this@MainActivity, NoteEditor::class.java)
            intent.putExtra("note", selectedNote)
            intent.putExtra("folder_id", selectedNote.folderId)
            startActivityForResult(intent, 104)
        }

        override fun onLongClick(note: Note, cardView: CardView) {
            selectedNote = note
            if (note.isDeleted) showNotePopUp(cardView)
            else showDemoNotePopUp(cardView)
        }
    }

    private fun showFolderPopUp(cardView: CardView) {
        val popupMenu: PopupMenu = PopupMenu(this, cardView)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.inflate(R.menu.folder_popup_menu)
        popupMenu.show()
    }

    private fun showDemoNotePopUp(cardView: CardView) {
        val popupMenu: PopupMenu = PopupMenu(this, cardView)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.inflate(R.menu.note_demo_popup_menu)
        popupMenu.show()
    }

    private fun showNotePopUp(cardView: CardView) {
        val popupMenu: PopupMenu = PopupMenu(this, cardView)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.inflate(R.menu.note_popup_menu)
        popupMenu.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.delete_folder -> {
                database.folderDao().delete(selectedFolder)
                folders.remove(selectedFolder)
                folderListAdapter.notifyDataSetChanged()
                Toast.makeText(this@MainActivity, "Folder removed", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.rename_folder -> {
                val intent = Intent(this@MainActivity, FolderEditor::class.java)
                intent.putExtra("old_folder", selectedFolder)
                startActivityForResult(intent, 102)
                return true
            }

            R.id.move_to_garbage -> {
                database.noteDao().moveToGarbage(selectedNote.id)
                noteList.remove(selectedNote)
                noteListAdapter.notifyDataSetChanged()
                Toast.makeText(this@MainActivity, "Note moved to garbage", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.restore_note -> {
                database.noteDao().restore(selectedNote.id)
                noteList.remove(selectedNote)
                noteListAdapter.notifyDataSetChanged()
                Toast.makeText(this@MainActivity, "Note restored", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.delete_note -> {
                database.noteDao().delete(selectedNote)
                noteList.remove(selectedNote)
                noteListAdapter.notifyDataSetChanged()
                Toast.makeText(this@MainActivity, "Note removed", Toast.LENGTH_SHORT).show()
                return true
            }

            else -> return false
        }
    }


}