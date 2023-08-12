package com.dbvertex.job.database

import androidx.room.*


@Dao
interface PodcastDAO {


    @Query("SELECT * FROM Podcast")
    fun getAllPodcast() : List<PodcastTableModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun  insert(vararg podcastTableModel: PodcastTableModel)

    @Delete
    fun  delete(podcastTableModel: PodcastTableModel)

    @Query("DELETE FROM Podcast")
    fun  deleteAllPodcast()

    @Update
    fun  update(podcastTableModel: PodcastTableModel)
}