package com.besirkaraoglu.newsapp.data

import com.besirkaraoglu.newsapp.model.News
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("everything?apiKey=97d9b8236a404f4193d0bf46e01a5145")
    suspend fun getSearchResults(
        @Query("q") query: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page:Int
    ): News

}