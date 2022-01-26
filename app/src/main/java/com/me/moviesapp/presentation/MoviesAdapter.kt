package com.me.moviesapp.presentation

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.me.moviesapp.R
import com.me.moviesapp.data.MoviesModel

class MoviesAdapter (
    private val moviesList: ArrayList<MoviesModel>
) : RecyclerView.Adapter<MoviesAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.movies_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = moviesList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(moviesList[position])

    fun addData(list: List<MoviesModel>) {
        moviesList.addAll(list)
    }

    fun getData(): ArrayList<MoviesModel>  {
        return moviesList;
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var movieTitle: TextView
         var posterImageView: ImageView
         var averageRating: TextView

        init {
            movieTitle = itemView.findViewById(R.id.movies_item_title)
            posterImageView = itemView.findViewById(R.id.movies_item_image)
            averageRating = itemView.findViewById(R.id.movies_item_rating)

        }

        fun bind(moviesModel: MoviesModel) {
            movieTitle.text = moviesModel.title
            averageRating.text = moviesModel.voteAverage.toString()
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500/"+moviesModel.posterPath)
                .into(posterImageView)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context,MovieDetails::class.java)
                     intent.putExtra("details",moviesModel)
                     itemView.context.startActivity(intent)
            }
        }
    }
}