package com.asykur.core.utils

import com.asykur.core.data.source.local.entity.MovieEntity
import com.asykur.core.data.source.remote.response.Movie
import com.asykur.core.domain.model.MovieResponse

object DataMapper {

    fun mapResponseToEntity(input: List<MovieResponse>): List<MovieEntity> {
        val mapResult = ArrayList<MovieEntity>()
        input.map {
            val movie = MovieEntity(
                id = it.id,
                adult = it.adult,
                backdrop_path = it.backdrop_path,
                isFavorite = false,
                original_language = it.original_language,
                original_title = it.original_title,
                title = it.title,
                overview = it.overview,
                popularity = it.popularity,
                poster_path = it.poster_path,
                release_date = it.release_date,
                video = it.video,
                vote_average = it.vote_average,
                vote_count = it.vote_count
            )
            mapResult.add(movie)
        }
        return mapResult
    }

    fun mapEntityToDomain(input: List<MovieEntity>): List<Movie> =
        input.map {
             Movie(
                id = it.id,
                adult = it.adult,
                backdrop_path = it.backdrop_path,
                isFavorite = it.isFavorite,
                original_language = it.original_language,
                original_title = it.original_title,
                title = it.title,
                overview = it.overview,
                popularity = it.popularity,
                poster_path = it.poster_path,
                release_date = it.release_date,
                video = it.video,
                vote_average = it.vote_average,
                vote_count = it.vote_count
            )
        }


    fun mapDomainToEntity(input: Movie) = MovieEntity(
        id = input.id,
        adult = input.adult,
        backdrop_path = input.backdrop_path,
        isFavorite = input.isFavorite,
        original_language = input.original_language,
        original_title = input.original_title,
        title = input.title,
        overview = input.overview,
        popularity = input.popularity,
        poster_path = input.poster_path,
        release_date = input.release_date,
        video = input.video,
        vote_average = input.vote_average,
        vote_count = input.vote_count
    )
}