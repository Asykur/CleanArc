package com.asykur.capstone1.di

import com.asykur.capstone1.presentation.viewmodel.DetailMovieViewModel
import com.asykur.capstone1.presentation.viewmodel.MainViewModel
import com.asykur.core.presentation.FavoriteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailMovieViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
}


