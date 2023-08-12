package com.dbvertex.job.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dbvertex.job.JobApp

@Database(entities = arrayOf(PodcastTableModel::class), version = 4)
abstract class PodcastDatabase : RoomDatabase() {
    abstract fun  podcast_Dao() : PodcastDAO

    companion object {
        private var instance: PodcastDatabase? = null

        fun getInstance() = instance ?: createDataBase().also { instance = it }

        private fun createDataBase() = Room.databaseBuilder(
            JobApp.instance,
            PodcastDatabase::class.java,
            "Podcast"
        ).fallbackToDestructiveMigration()
         .allowMainThreadQueries()
        .build()
    }

}