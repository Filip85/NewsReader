package com.filip.newsreader.Retrofit

import com.filip.newsreader.DatabaseTable.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://newsapi.org/"

interface NewsApi {

    @GET("v1/articles")
    fun showNews(@Query("source") source: String,
                 @Query("sortBy") sortBy: String,
                 @Query("apiKey") apiKey: String): Call<News>
}