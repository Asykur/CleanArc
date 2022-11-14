package com.asykur.capstone1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asykur.core.data.source.remote.response.Movie
import com.asykur.core.domain.usecase.MovieUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailMovieViewModel(private val useCase: MovieUseCase): ViewModel() {

    fun setFavoriteMovie(movie: Movie, isFavorite: Boolean){
        viewModelScope.launch(Dispatchers.IO){
            useCase.setFavoriteMovie(movie, isFavorite)
        }
    }
}