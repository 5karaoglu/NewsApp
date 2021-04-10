package com.besirkaraoglu.newsapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "table_article")
data class ArticleDbEntity(
    val author: String,
    @PrimaryKey(autoGenerate = false)
    val title: String,
    val description: String,
    val url: String,
    @ColumnInfo(name = "url_to_image")
    val urlToImage: String,
    @ColumnInfo(name = "published_at")
    val publishedAt: String,
    val content: String,
    @ColumnInfo(name = "source_id")
    val sourceId: String? = null,
    @ColumnInfo(name = "source_name")
    val sourceName: String
): Parcelable
