package com.asykur.capstone1.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.asykur.capstone1.R
import com.asykur.capstone1.databinding.FragmentMainBinding
import com.asykur.capstone1.presentation.adapter.MovieAdapter
import com.asykur.capstone1.presentation.uistate.NowPlayingUiState
import com.asykur.capstone1.presentation.viewmodel.MainViewModel
import com.asykur.core.BuildConfig
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModel()
    private val rvAdapter = MovieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.apply {
            ivFavorite.setOnClickListener {
                try {
                    findNavController().navigate(R.id.favoriteGraph)
                } catch (e: Exception) {
                    _binding?.let { it1 ->
                        Snackbar.make(
                            it1.root,
                            "${e.message}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            etSearch.addTextChangedListener {
                if (etSearch.hasFocus()){
                    rvAdapter.filter.filter(it.toString())
                }
            }
        }
        fetchMovie()
        initObserver()
    }

    private fun fetchMovie() {
        viewModel.getNowPlaying(BuildConfig.API_KEY, "en", 1)
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.nowPlayingUiState.collect { uiState ->
                _binding?.apply {

                    when (uiState) {
                        is NowPlayingUiState.ShowLoading -> {
                            pgMovie.visibility = View.VISIBLE
                        }
                        is NowPlayingUiState.ShowMovies -> {
                            pgMovie.visibility = View.GONE

                            if (uiState.movie != null) {
                                rvNowPlaying.apply {
                                    rvAdapter.submitList(uiState.movie)
                                    adapter = rvAdapter
                                    layoutManager = GridLayoutManager(requireContext(), 2)

                                    rvAdapter.onItemClicked = { ivMovie, movie ->
                                        val extras = FragmentNavigatorExtras(ivMovie to "imgDetail")
                                        findNavController().navigate(
                                            R.id.action_mainFragment_to_detailMovieFragment,
                                            bundleOf("movie" to movie),
                                            null,
                                            extras
                                        )
                                    }
                                }

                            }
                        }
                        is NowPlayingUiState.ShowError -> {
                            pgMovie.visibility = View.GONE
                            Snackbar.make(
                                root,
                                uiState.errorMessage ?: "Unknown Error",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvNowPlaying.adapter = null
        _binding = null
    }
}