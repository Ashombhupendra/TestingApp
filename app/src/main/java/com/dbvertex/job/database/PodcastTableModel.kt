package com.dbvertex.job.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Podcast")
data class PodcastTableModel(
    @PrimaryKey
    var id : String,
    var title : String,
    var description : String,
    var created : String,
    var audio : String,
    var duration : Int,
    var favourite : Boolean
)
