package com.besirkaraoglu.newsapp.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.besirkaraoglu.newsapp.model.Article
import com.besirkaraoglu.newsapp.model.ArticleDbEntity
import com.besirkaraoglu.newsapp.model.News
import com.besirkaraoglu.newsapp.util.DataState
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


class NewsRepository
@Inject constructor(
    private val dao: NewsDao,
    private val api: NewsApi
){
    private val TAG ="Repository"

    suspend fun getResults(query: String, pageSize: Int, page: Int): Flow<DataState<News>> = flow{
        emit(DataState.Loading)
        try {
            val result = api.getSearchResults(query,pageSize,page)
            emit(DataState.Success(result))

        }catch (cause:NewsError){
            emit(DataState.Error(cause))
        }

    }
    suspend fun getSearchResults(query: String): Flow<PagingData<Article>> {
        Log.d(TAG, "getSearchResults: repository")
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {NewsPagingDataSource(api, query)}
        ).flow
    }

    suspend fun getFavorites(): Flow<DataState<List<ArticleDbEntity>>> = flow {
        emit(DataState.Loading)
        try {
            val articles = dao.getAll()
            emit(DataState.Success(articles))

        }catch (cause: Throwable){
            emit(DataState.Error(cause))
        }
    }

    suspend fun insert(article: Article) = withContext(Dispatchers.IO){
        try {
            val articleDbEntity = ArticleDbEntity(article.author,article.title,article.description,article.url,
                article.urlToImage,article.publishedAt,article.content,article.source.id,article.source.name)
            dao.insert(articleDbEntity)
        }catch (cause:NewsError){
            throw NewsError("",cause)
        }
    }

    suspend fun delete(articleDbEntity: ArticleDbEntity) = withContext(Dispatchers.IO) {
        try {
            dao.delete(articleDbEntity)
        } catch (cause: NewsError) {
            throw NewsError("", cause)
        }
    }

    suspend fun deleteAll() = withContext(Dispatchers.IO){
        dao.deleteAll()
    }

}
class NewsError(message: String, cause: Throwable): Throwable(message, cause)