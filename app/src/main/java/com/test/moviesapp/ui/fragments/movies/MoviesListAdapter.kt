package com.test.moviesapp.ui.fragments.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.test.moviesapp.R
import com.test.moviesapp.data.entities.Favorite
import com.test.moviesapp.databinding.MovieListItemBinding
import com.test.moviesapp.ui.fragments.entitiesUI.MovieListUI
import com.test.moviesapp.utils.addEllipsis

class MoviesListAdapter(
    private val liveDataExternalId: MutableLiveData<String>,
    private val liveDataSetFavorite: MutableLiveData<Favorite>? = null,
    private val withFavorites : Boolean = true
) :
    RecyclerView.Adapter<MoviesListViewHolder>() {

    private val items = ArrayList<MovieListUI>()

    fun setItems(items: ArrayList<MovieListUI>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesListViewHolder {
        val binding: MovieListItemBinding =
            MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviesListViewHolder(binding, withFavorites)
    }

    override fun onBindViewHolder(holder: MoviesListViewHolder, position: Int) =
        holder.bind(items[position], itemCount, position, liveDataExternalId, liveDataSetFavorite)

}


class MoviesListViewHolder(
    private val itemBinding: MovieListItemBinding,
    private val withFavorites: Boolean
) : RecyclerView.ViewHolder(itemBinding.root) {

    private val iconMap = hashMapOf(
        true to R.drawable.ic_favorite,
        false to R.drawable.ic_not_favorite
    )

    fun bind(
        item: MovieListUI,
        itemCount: Int,
        position: Int,
        goToDetails: MutableLiveData<String>,
        liveDataSetFavorite: MutableLiveData<Favorite>? = null
    ) {
        with(itemBinding) {
            movieTitle.text = item.name
            movieDescription.text = item.shortDescription.addEllipsis()
            moviePR.text = item.prLevel?.description
            Glide.with(root)
                .load("https://smarttv.orangetv.orange.es/stv/api/rtv/v1/images/${item.cover}")
                .transform(RoundedCorners(20))
                .into(itemBinding.coverImage)
            layoutBack.background = itemView.context.getDrawable(
                when (position) {
                    itemCount - 1 -> R.drawable.background_movie_list_last
                    else -> R.drawable.background_movie_list
                }
            )
            layoutBack.setOnClickListener {
                goToDetails.postValue(item.externalId)
            }
            favoriteIcon.let { icon ->
                iconMap.entries.find { it.key == item.isFavorite }?.value?.let {
                    icon.setImageResource(it)
                }
                liveDataSetFavorite?.let { liveData ->
                    icon.setOnClickListener {
                        item.isFavorite = !item.isFavorite
                        iconMap.entries.find { it.key == item.isFavorite }?.value?.let {
                            icon.setImageResource(it)
                        }
                        with(item) {
                            liveData.value =
                                Favorite(
                                    assetExternalId,
                                    shortDescription,
                                    externalId,
                                    name,
                                    prLevel?.prLevel ?: -1,
                                    cover
                                )
                        }
                    }
                }
            }
            favoriteIcon.visibility = if (withFavorites) View.VISIBLE else View.GONE
        }
    }

}