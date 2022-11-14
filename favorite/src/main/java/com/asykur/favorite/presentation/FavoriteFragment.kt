package com.asykur.favorite.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.asykur.capstone1.presentation.adapter.MovieAdapter
import com.asykur.core.data.source.remote.response.Movie
import com.asykur.core.presentation.FavoriteViewModel
import com.asykur.favorite.R
import com.asykur.favorite.databinding.FragmentFavoriteBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private val viewModel : FavoriteViewModel by viewModel()
    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.apply {
            title = context.getString(R.string.favorite)
            navigationIcon = ContextCompat.getDrawable(requireContext(), com.asykur.core.R.drawable.ic_arrow_back)
            setNavigationOnClickListener { findNavController().popBackStack() }
        }

        getFavoriteMovie()
        initObserver()
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.favoriteList.collect{
                    if (it.isEmpty()){
                        binding.clEmpty.visibility = View.VISIBLE
                    }else{
                        showFavoriteMovie(it)
                    }
                }
            }
        }
    }

    private fun showFavoriteMovie(list: List<Movie>) {
        binding.clEmpty.visibility = View.GONE
        binding.rvFavorite.apply {
            val rvAdapter = MovieAdapter()
            rvAdapter.submitList(list)
            rvAdapter.onItemClicked = { _, movie ->
                findNavController().navigate(R.id.action_favoriteFragment_to_detailMovieFragment2,
                bundleOf("movie" to movie)
                )
            }
            adapter = rvAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun getFavoriteMovie() {
        viewModel.getFavoriteMovie()
    }


}