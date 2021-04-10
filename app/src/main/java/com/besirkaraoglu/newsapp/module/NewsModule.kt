package com.besirkaraoglu.newsapp.module

import android.content.Context
import androidx.room.Room
import com.besirkaraoglu.newsapp.data.NewsApi
import com.besirkaraoglu.newsapp.data.NewsDao
import com.besirkaraoglu.newsapp.data.NewsDatabase
import com.besirkaraoglu.newsapp.data.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NewsModule {

    @Singleton
    @Provides
    fun provideRepository(
        dao: NewsDao,
        api: NewsApi
    ): NewsRepository{
        return NewsRepository(dao,api)
    }

    @Singleton
    @Provides
    fun provideApi(): NewsApi{
        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context): NewsDatabase{
        return  Room.databaseBuilder(context,
        NewsDatabase::class.java,
        NewsDatabase.DB_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(db: NewsDatabase): NewsDao{
        return db.dao()
    }
}