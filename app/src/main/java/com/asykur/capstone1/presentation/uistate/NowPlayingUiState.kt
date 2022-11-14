package com.asykur.capstone1.presentation.uistate

import com.asykur.core.data.source.remote.response.Movie

sealed class NowPlayingUiState {
    object ShowLoading: NowPlayingUiState()
    data class ShowMovies(val movie: List<Movie>?): NowPlayingUiState()
    data class ShowError(val errorMessage: String?) : NowPlayingUiState()

}
