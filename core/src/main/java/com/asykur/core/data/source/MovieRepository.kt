package com.asykur.core.data.source

import com.asykur.core.data.Resource
import com.asykur.core.data.repository.IMovieRepository
import com.asykur.core.data.source.local.LocalDataSource
import com.asykur.core.data.source.remote.RemoteDataSource
import com.asykur.core.data.source.remote.response.Movie
import com.asykur.core.domain.model.MovieResponse
import com.asykur.core.utils.AppExecutor
import com.asykur.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutor
) : IMovieRepository {

    override fun getNowPlaying(
        apikey: String,
        language: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return object : NetworkBoundResource<List<Movie>, List<MovieResponse>>(){
            override fun loadFromDB(): Flow<List<Movie>> {
                return localDataSource.getAllMovie().map {
                    DataMapper.mapEntityToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Movie>?): Boolean {
                return data == null || data.isEmpty()
            }

            override suspend fun createCall(): Flow<ApiResponse<List<MovieResponse>>> {
                return remoteDataSource.getNowPlaying(apikey,language,page)
            }

            override suspend fun saveCallResult(data: List<MovieResponse>) {
                val movieList = DataMapper.mapResponseToEntity(data)
                localDataSource.insertMovie(movieList)
            }
        }.asFlow()
    }

    override fun getFavoriteMovie(): Flow<List<Movie>> {
        return localDataSource.getFavoriteMovie().map {
            DataMapper.mapEntityToDomain(it)
        }
    }

    override fun setFavoriteMovie(movie: Movie, state: Boolean) {
        val entity = DataMapper.mapDomainToEntity(movie)
        appExecutors.diskIO().execute{localDataSource.setFavoriteMovie(entity, state)}
    }

}