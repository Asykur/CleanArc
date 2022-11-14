package com.asykur.capstone1.presentation.fragment

import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.asykur.capstone1.R
import com.asykur.capstone1.databinding.FragmentDetailMovieBinding
import com.asykur.capstone1.presentation.viewmodel.DetailMovieViewModel
import com.asykur.core.BuildConfig
import com.asykur.core.data.source.remote.response.Movie
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailMovieFragment : Fragment() {

    private var _binding: FragmentDetailMovieBinding? = null
    private val binding get() = _binding!!

    private var movie: Movie? = null
    private val viewModel: DetailMovieViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movie = if (Build.VERSION.SDK_INT >= 33) {
                it.getParcelable("movie", Movie::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable("movie")
            }
        }
        val animation =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.apply {
            title = context.getString(R.string.detail_movie)
            navigationIcon = ContextCompat.getDrawable(
                requireContext(),
                com.asykur.core.R.drawable.ic_arrow_back
            )
            setNavigationOnClickListener { findNavController().popBackStack() }
        }

        movie?.let { movie ->
            Glide.with(requireActivity())
                .load("${BuildConfig.BASE_URL_IMAGE}${movie.poster_path}")
                .into(binding.ivDetailMovie)
            binding.tvOverview.text = movie.overview
            binding.tvRate.text = String.format("${movie.vote_average} %s"," / 10")
            binding.tvTitle.text = movie.title

            var favoriteStatus = movie.isFavorite
            updateFavoriteButton(false, favoriteStatus)
            binding.btnFavorite.setOnClickListener {
                favoriteStatus = !favoriteStatus
                viewModel.setFavoriteMovie(movie, favoriteStatus)
                updateFavoriteButton(true, favoriteStatus)
            }
        }

    }

    private fun updateFavoriteButton(isClicked: Boolean, isFavorite: Boolean) {
        if (isFavorite) {
            if (isClicked){
                showSnackBar("${movie?.title} added to favorite")
            }
            binding.btnFavorite.setImageResource(R.drawable.ic_star)
        } else {
            if (isClicked){
                showSnackBar("${movie?.title} removed from favorite")
            }
            binding.btnFavorite.setImageResource(R.drawable.ic_star_unselected)
        }
    }

    private fun showSnackBar( msg: String){
        Snackbar.make(
            binding.root,
            msg,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}