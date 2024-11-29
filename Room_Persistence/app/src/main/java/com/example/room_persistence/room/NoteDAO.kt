package com.example.room_persistence.room

import android.security.identity.AccessControlProfileId
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDAO {
    @Insert
    suspend fun insert(note: Note)

    @Query("Select*From notes")
    suspend fun getAllNotes(): List<Note>

    @Query("Select*From notes Where id = :id")
    suspend fun getNoteById(id:Int):Note?

    @Query("Delete From notes Where id = :id")
    suspend fun deleteById(id: Int)
}