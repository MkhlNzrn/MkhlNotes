package ru.itmo.notes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.annotations.NotNull
import ru.itmo.notes.R
import ru.itmo.notes.dataLayer.entity.Folder
import ru.itmo.notes.listener.FolderClickListener

class FolderListAdapter(
    private val context: Context,
    private val folderList: List<Folder>,
    private val listener: FolderClickListener
) : RecyclerView.Adapter<FolderListAdapter.FolderViewHolder>() {
    @NotNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): FolderViewHolder {
        return FolderViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.folders_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(@NonNull holder: FolderViewHolder, position: Int) {
        holder.textViewFolderName.text = folderList[position].folderName
        holder.textViewFolderName.isSelected = true

        holder.folderContainer.setCardBackgroundColor(
            holder.itemView.resources.getColor(
                R.color.teal_200, null
            )
        )

        holder.folderContainer.setOnClickListener {
            listener.onClick(folderList[holder.adapterPosition])
        }

        holder.folderContainer.setOnLongClickListener {
            listener.onLongClick(folderList[holder.adapterPosition], holder.folderContainer)
            true
        }
    }

    override fun getItemCount(): Int {
        return folderList.size
    }

    class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val folderContainer: CardView = itemView.findViewById(R.id.folder_container)
        val textViewFolderName: TextView = itemView.findViewById(R.id.textView_folderName)
    }
}
