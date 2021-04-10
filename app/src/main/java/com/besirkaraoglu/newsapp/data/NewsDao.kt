package com.besirkaraoglu.newsapp.data

import androidx.room.*
import com.besirkaraoglu.newsapp.model.Article
import com.besirkaraoglu.newsapp.model.ArticleDbEntity

@Dao
interface NewsDao {
    @Query("SELECT * FROM table_article")
    suspend fun getAll():List<ArticleDbEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articleDbEntity: ArticleDbEntity)

    @Delete
    suspend fun delete(articleDbEntity: ArticleDbEntity)

    @Query("DELETE FROM table_article")
    suspend fun deleteAll()
}