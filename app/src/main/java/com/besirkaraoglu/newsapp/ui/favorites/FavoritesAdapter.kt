package com.besirkaraoglu.newsapp.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.besirkaraoglu.newsapp.R
import com.besirkaraoglu.newsapp.model.ArticleDbEntity
import com.besirkaraoglu.newsapp.util.Functions
import com.squareup.picasso.Picasso

class FavoritesAdapter(private val listener: OnItemClickListener):
    ListAdapter<ArticleDbEntity, FavoritesAdapter.FavoritesViewHolder>(CustomComparator()) {

    class FavoritesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val TAG = "Favorites ViewHolder"

        val imageView = itemView.findViewById<ImageView>(R.id.iv)
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvDesc = itemView.findViewById<TextView>(R.id.tvDescription)
        val tvSource = itemView.findViewById<TextView>(R.id.tvSource)
        val tvDate = itemView.findViewById<TextView>(R.id.tvDate)

        fun bind(articleDbEntity: ArticleDbEntity, listener: OnItemClickListener,
                 position: Int){
            Picasso.get()
                .load(articleDbEntity.urlToImage)
                .error(R.drawable.ic_baseline_cancel_24)
                .centerCrop().fit()
                .into(imageView)
            tvTitle.text = articleDbEntity.title
            tvDesc.text = articleDbEntity.description
            tvSource.text = articleDbEntity.author
            tvDate.text = Functions.formatDate(articleDbEntity.publishedAt)

            itemView.setOnClickListener {
                listener.onItemClicked(articleDbEntity)
            }
            itemView.setOnLongClickListener {
                listener.onItemLongClicked(articleDbEntity,position)
            }
        }
    }


    interface OnItemClickListener{
        fun onItemClicked(articleDbEntity: ArticleDbEntity)
        fun onItemLongClicked(articleDbEntity: ArticleDbEntity, position: Int): Boolean
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_news_item,parent,false)
        return FavoritesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem,listener,position)
        }
    }
}
class CustomComparator(): DiffUtil.ItemCallback<ArticleDbEntity>(){
    override fun areItemsTheSame(oldItem: ArticleDbEntity, newItem: ArticleDbEntity): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: ArticleDbEntity, newItem: ArticleDbEntity): Boolean {
        return oldItem.title == newItem.title
    }

}