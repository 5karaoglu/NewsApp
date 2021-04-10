package com.besirkaraoglu.newsapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.besirkaraoglu.newsapp.model.Article
import com.besirkaraoglu.newsapp.model.News
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val START_PAGE_NUMBER = 1

class NewsPagingDataSource(
    private val api: NewsApi,
    private val query: String
): PagingSource<Int,Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val pageNumber = params.key ?: START_PAGE_NUMBER
        return try {
            val response = api.getSearchResults(query, params.loadSize,pageNumber)
            val articles = response.articles
            LoadResult.Page(
                data = articles,
                prevKey = if (pageNumber == START_PAGE_NUMBER) null else pageNumber - 1,
                nextKey = if (articles.isEmpty()) null else pageNumber + 1
            )
        }catch (ex: IOException){
            Log.d("d", "load: error1 ${ex.message}")
            LoadResult.Error(ex)
           }
         catch (ex: HttpException){
             Log.d("d", "load: error2 ${ex.message}")
             LoadResult.Error(ex)}
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }


}