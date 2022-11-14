package com.asykur.core.domain.usecase

import com.asykur.core.data.Resource
import com.asykur.core.data.repository.IMovieRepository
import com.asykur.core.data.source.remote.response.Movie
import kotlinx.coroutines.flow.Flow

class MovieInteractor(private val repository: IMovieRepository) : MovieUseCase {
    override fun getNowPlaying(
        apikey: String,
        language: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return repository.getNowPlaying(apikey, language, page)
    }

    override fun getFavoriteMovie(): Flow<List<Movie>> {
        return repository.getFavoriteMovie()
    }

    override fun setFavoriteMovie(movie: Movie, state: Boolean) {
        return repository.setFavoriteMovie(movie, state)
    }

}