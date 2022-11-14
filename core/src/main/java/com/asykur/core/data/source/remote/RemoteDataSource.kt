package com.asykur.core.data.source.remote

import com.asykur.core.data.source.ApiResponse
import com.asykur.core.domain.model.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiServices: ApiServices) {

    suspend fun getNowPlaying(
        apikey: String,
        language: String,
        page: Int
    ): Flow<ApiResponse<List<MovieResponse>>> =
        flow {
        val response = apiServices.getNowPlaying(apikey, language, page)
        if (response.results.isNotEmpty()) {
            emit(ApiResponse.Success(response.results))
        } else {
            emit(ApiResponse.Empty)
        }

    }.catch {e ->
        emit(ApiResponse.Error(e.message.toString()))
    }.flowOn(Dispatchers.IO)

}