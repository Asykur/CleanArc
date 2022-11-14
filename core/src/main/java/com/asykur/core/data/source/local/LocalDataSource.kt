package com.asykur.core.data.source.local

import com.asykur.core.data.source.local.entity.MovieEntity
import com.asykur.core.data.source.local.room.MovieDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val movieDao: MovieDao) {

    fun getAllMovie(): Flow<List<MovieEntity>> = movieDao.getAllMovies()

    fun getFavoriteMovie(): Flow<List<MovieEntity>> = movieDao.getFavoriteMovies()

    suspend fun insertMovie(movieList: List<MovieEntity>) = movieDao.insertMovieList(movieList)

    fun setFavoriteMovie(movie: MovieEntity, newState: Boolean) {
        movie.isFavorite = newState
        movieDao.updateFavoriteMovie(movie)
    }

}