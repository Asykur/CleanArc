package com.asykur.core.data.repository

import com.asykur.core.data.Resource
import com.asykur.core.data.source.remote.response.Movie
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    fun getNowPlaying(
        apikey:String,
        language: String,
        page:Int
    ):Flow<Resource<List<Movie>>>

    fun getFavoriteMovie():Flow<List<Movie>>
    fun setFavoriteMovie(movie: Movie, state: Boolean)
}