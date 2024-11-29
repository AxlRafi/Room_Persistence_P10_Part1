package com.example.room_persistence.room
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "notes")
class Note (
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val title:String,
    val content:String,
    val timestamp:Timestamp,
)