package com.asykur.core.data.source.remote

import com.asykur.core.data.source.remote.response.BaseMovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("api_key")api_key:String,
        @Query("language")language:String,
        @Query("page")page:Int
    ): BaseMovieResponse

}