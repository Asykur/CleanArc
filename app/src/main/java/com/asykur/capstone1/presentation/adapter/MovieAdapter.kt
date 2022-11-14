package com.asykur.capstone1.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.asykur.capstone1.databinding.ItemSimpleMovieBinding
import com.asykur.core.BuildConfig
import com.asykur.core.data.source.remote.response.Movie
import com.bumptech.glide.Glide

class MovieAdapter : ListAdapter<Movie, MovieAdapter.VH>(MovieDiffCallback), Filterable {
    private var filteredList = mutableListOf<Movie>()
    private var movieList = listOf<Movie>()
    var onItemClicked: ((View, Movie) -> Unit)? = null

    object MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCurrentListChanged(
        previousList: MutableList<Movie>,
        currentList: MutableList<Movie>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        this.movieList = currentList
        this.filteredList = movieList.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            ItemSimpleMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(filteredList[position])
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    inner class VH(private val itemBinding: ItemSimpleMovieBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(movie: Movie) {
            Glide.with(itemBinding.root)
                .load("${BuildConfig.BASE_URL_IMAGE}${movie.poster_path}")
                .into(itemBinding.ivMovie)
            itemBinding.tvTitle.text = movie.title

            itemBinding.root.setOnClickListener {
                onItemClicked?.invoke(itemBinding.ivMovie, movie)
            }
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(char: CharSequence?): FilterResults {
                val query = char.toString()
                filteredList = if (query.isEmpty()) {
                    movieList.toMutableList()
                } else {
                    val filter = mutableListOf<Movie>()
                    filteredList
                        .filter {
                            it.title.lowercase().contains(query, true)
                        }.forEach { data ->
                            filter.add(data)
                        }
                    filter
                }
                return FilterResults().apply { values = filteredList }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(char: CharSequence?, results: FilterResults?) {
                val resultData = results?.values as ArrayList<Movie>
                filteredList = if (resultData.size == 0) {
                    mutableListOf()
                } else {
                    resultData
                }
                notifyDataSetChanged()
            }

        }
    }
}