package com.filip.newsreader.Database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.filip.newsreader.Dao.NewsDao
import com.filip.newsreader.DatabaseTable.Articles
import com.filip.newsreader.DatabaseTable.Time
import com.filip.newsreader.MyApplication

@Database(version = 2, entities = arrayOf(Articles::class, Time::class))
abstract class NewsDatabase: RoomDatabase() {

    abstract fun newsDao(): NewsDao
    companion object {
        private val instance by lazy {
            Room.databaseBuilder(MyApplication.ApplicationContext, NewsDatabase::class.java, "news")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
        }

        val newsDao: NewsDao by lazy { instance.newsDao() }

    }

}