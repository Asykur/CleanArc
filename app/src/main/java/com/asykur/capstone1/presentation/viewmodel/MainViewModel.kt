package com.asykur.capstone1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asykur.core.data.Resource
import com.asykur.core.domain.usecase.MovieUseCase
import com.asykur.capstone1.presentation.uistate.NowPlayingUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val useCase: MovieUseCase) : ViewModel() {

    private val _nowPlayingUiState = MutableSharedFlow<NowPlayingUiState>()
    var nowPlayingUiState : MutableSharedFlow<NowPlayingUiState> = _nowPlayingUiState

    fun getNowPlaying(
        apikey: String,
        language: String,
        page: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getNowPlaying(apikey, language, page).collect { resource ->
                withContext(Dispatchers.Main) {
                    val state = when (resource) {
                        is Resource.Loading -> {
                            NowPlayingUiState.ShowLoading
                        }
                        is Resource.Success -> {
                            NowPlayingUiState.ShowMovies(resource.data)
                        }
                        is Resource.Error -> {
                            NowPlayingUiState.ShowError(resource.message)
                        }
                    }
                    _nowPlayingUiState.emit(state)
                }
            }
        }
    }
}