package com.besirkaraoglu.newsapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.besirkaraoglu.newsapp.R
import com.besirkaraoglu.newsapp.model.Article
import com.besirkaraoglu.newsapp.util.Functions
import com.squareup.picasso.Picasso

class NewsAdapter(
    private val listener: OnItemClickListener
): PagingDataAdapter<Article, NewsAdapter.NewsViewHolder>(ARTICLE_COMPARATOR) {

    class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val TAG = "News ViewHolder"

        val imageView = itemView.findViewById<ImageView>(R.id.iv)
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvDesc = itemView.findViewById<TextView>(R.id.tvDescription)
        val tvSource = itemView.findViewById<TextView>(R.id.tvSource)
        val tvDate = itemView.findViewById<TextView>(R.id.tvDate)

        fun bind(article: Article, listener: OnItemClickListener){
            Picasso.get()
                .load(article.urlToImage)
                .error(R.drawable.ic_baseline_cancel_24)
                .centerCrop().fit()
                .into(imageView)
            tvTitle.text = article.title
            tvDesc.text = article.description
            tvSource.text = article.author
            tvDate.text = Functions.formatDate(article.publishedAt)

            itemView.setOnClickListener {
                listener.onItemClicked(article)
            }
        }

    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem,listener)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_news_item,parent,false)
       return NewsViewHolder(view)
    }

    interface OnItemClickListener{
        fun onItemClicked(article: Article)
    }

    companion object{
        private val ARTICLE_COMPARATOR = object : DiffUtil.ItemCallback<Article>(){
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: Article,
                newItem: Article
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}