package ru.itmo.notes.listener

import androidx.cardview.widget.CardView
import ru.itmo.notes.dataLayer.entity.Folder

interface FolderClickListener {
    fun onClick(folder: Folder)
    fun onLongClick(folder: Folder, cardView: CardView)
}