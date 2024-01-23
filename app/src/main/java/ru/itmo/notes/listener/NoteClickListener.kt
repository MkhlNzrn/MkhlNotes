package ru.itmo.notes.listener

import androidx.cardview.widget.CardView
import ru.itmo.notes.dataLayer.entity.Note

interface NoteClickListener {
    fun onClick(note: Note)
    fun onLongClick(note: Note, cardView: CardView)
}