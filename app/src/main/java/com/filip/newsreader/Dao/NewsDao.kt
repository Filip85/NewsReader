package com.filip.newsreader.Dao

import android.arch.persistence.room.*
import com.filip.newsreader.DatabaseTable.Articles
import com.filip.newsreader.DatabaseTable.Time

@Dao
interface NewsDao {

    @Insert
    fun insert(a: Articles)

    @Query("SELECT * FROM articles")
    fun getAll(): List<Articles>

    @Delete
    fun deleteAllNews(a: Articles)

    @Query("SELECT COUNT(*) FROM articles")
    fun count(): Int

    @Query("SELECT time FROM time")
    fun getTime(): Int

    @Insert
    fun insertTime(t: Time)

    @Query("UPDATE time SET time = :newTime WHERE id = 1")
    fun updateTime(newTime: Int)


}