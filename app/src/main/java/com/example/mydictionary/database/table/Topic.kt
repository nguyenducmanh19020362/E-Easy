package com.example.mydictionary.database.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "Topic",
    foreignKeys = [
        ForeignKey(
            entity = Folder::class,
            parentColumns = ["id"],
            childColumns = ["id_folder"],
            onDelete = CASCADE
        )
    ]
)
data class Topic (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int?,

    @ColumnInfo(name = "topic_name")
    var topic_name: String,

    @ColumnInfo(name = "id_folder")
    var id_folder: Int
)