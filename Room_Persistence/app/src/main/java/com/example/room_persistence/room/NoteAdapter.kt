package com.example.room_persistence.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.room_persistence.R

class NoteAdapter(private val notes: List<Note>) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int = notes.size

    class NoteViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(note: Note) {
            val titleTextView = view.findViewById<TextView>(R.id.editTextTitle)
            val contentTextView = view.findViewById<TextView>(R.id.textViewContent)
            val timestampTextView = view.findViewById<TextView>(R.id.textViewTimes)

            titleTextView.text = note.title
            contentTextView.text = note.content
            timestampTextView.text = note.timestamp
        }
    }
}