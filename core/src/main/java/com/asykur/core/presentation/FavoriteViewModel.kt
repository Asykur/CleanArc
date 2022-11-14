package com.asykur.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asykur.core.data.source.remote.response.Movie
import com.asykur.core.domain.usecase.MovieUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val useCase: MovieUseCase): ViewModel() {


    private val _favoriteList = MutableSharedFlow<List<Movie>>()
    var favoriteList : MutableSharedFlow<List<Movie>> = _favoriteList

    fun getFavoriteMovie(){
        viewModelScope.launch(Dispatchers.IO){
            useCase.getFavoriteMovie().collect{
                if (it.isNotEmpty()){
                    _favoriteList.emit(it)
                }else{
                    _favoriteList.emit(emptyList())
                }
            }
        }
    }
}