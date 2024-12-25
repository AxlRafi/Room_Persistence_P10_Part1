//MainActivity
package com.example.room_persistence


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.room_persistence.room.MainViewModel
import com.example.room_persistence.room.Note
import com.example.room_persistence.room.NoteAdapter
import com.example.room_persistence.ui.AddNoteActivity
import com.example.room_persistence.ui.EditNoteActivity

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var mainViewModel: MainViewModel

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if (result.resultCode == Activity.RESULT_OK ){
            Toast.makeText(this ,"Note saved successfully", Toast.LENGTH_SHORT).show()
            mainViewModel.fetchNotes()
        }
    }

    private val startForResultEdit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK ){
            val updatedNote= result.data?.getSerializableExtra("updatedNote") as Note

            updatedNote?.let {
                Toast.makeText(this ,"Note Updated successfully", Toast.LENGTH_SHORT).show()
                mainViewModel.fetchNotes()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


        }

        recyclerView = findViewById(R.id.recyclerViewNotes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.allNotes.observe(this){notes ->
            updateRecyclerView(notes)
        }


        val buttonAddNote: Button = findViewById(R.id.buttonAddNote)
        buttonAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity:: class.java)
            startForResult.launch(intent)
        }
    }

    private fun updateRecyclerView(notes: List<Note>) {
        noteAdapter = NoteAdapter(notes,
            onDeleteListener = { noteId -> showDeleteConfirmationDialog(noteId)},
            onEditListener = {note -> showEditConfirmationDialog(note)} )

        recyclerView.adapter=noteAdapter
        noteAdapter.notifyDataSetChanged()
    }

    private fun showDeleteConfirmationDialog(noteId: Int){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("delete note")
            .setMessage("Are you sure you want to delete this Note?")
            .setPositiveButton("Yes"){dialog, _ ->
                mainViewModel.deleteNoteById(noteId)
                Toast.makeText(this, "Note Successfully been Deleted", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                mainViewModel.fetchNotes()
            }
            .setNegativeButton("No"){
                dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()


    }


    private fun showEditConfirmationDialog(note:Note) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Note")
            .setMessage("Are you you want to edit this Note?")
            .setPositiveButton("Yes") { dialog, _ ->
                val intent = Intent(this, EditNoteActivity::class.java)
                intent.putExtra("note", note)
                startForResultEdit.launch(intent)
                dialog.dismiss()
            }
            .setNegativeButton("No") {
                dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}