package ru.itmo.notes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.annotations.NotNull
import ru.itmo.notes.R
import ru.itmo.notes.dataLayer.entity.Note
import ru.itmo.notes.listener.NoteClickListener

class NoteListAdapter(
    private val context: Context,
    private val list: List<Note>,
    private val listener: NoteClickListener
) : RecyclerView.Adapter<NoteViewHolder>() {
    @NotNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.notes_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.textViewNoteTitle.text = list[position].noteTitle
        holder.textViewNoteTitle.isSelected = true

        holder.textViewNoteText.text = list[position].noteText
        holder.textViewNoteText.isSelected = true

        holder.noteContainer.setCardBackgroundColor(
            holder.itemView.resources.getColor(
                R.color.purple_200, null
            )
        )

        holder.noteContainer.setOnClickListener {
            listener.onClick(list[holder.adapterPosition])
        }

        holder.noteContainer.setOnLongClickListener {
            listener.onLongClick(list[holder.adapterPosition], holder.noteContainer)
            true
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val noteContainer: CardView = itemView.findViewById(R.id.note_container)
    val textViewNoteTitle: TextView = itemView.findViewById(R.id.textView_noteTitle)
    val textViewNoteText: TextView = itemView.findViewById(R.id.textView_noteText)
}
