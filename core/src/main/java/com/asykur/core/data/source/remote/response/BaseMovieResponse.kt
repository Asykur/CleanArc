package com.asykur.core.data.source.remote.response

import android.os.Parcelable
import com.asykur.core.domain.model.MovieResponse
import kotlinx.parcelize.Parcelize

data class BaseMovieResponse(
    val page: Int,
    val results: List<MovieResponse>,
    val total_pages: Int,
    val total_results: Int
)

@Parcelize
data class Movie(
    val adult: Boolean,
    val backdrop_path: String,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    var isFavorite : Boolean
): Parcelable