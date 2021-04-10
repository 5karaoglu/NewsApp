package com.besirkaraoglu.newsapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.besirkaraoglu.newsapp.model.ArticleDbEntity

@Database(entities = [ArticleDbEntity::class],version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase(){
    companion object{
       const val DB_NAME = "db_news"
    }

    abstract fun dao() : NewsDao
}