package com.besirkaraoglu.newsapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.besirkaraoglu.newsapp.R

class NewsLoadStateAdapter(
        private val retry: () -> Unit
): LoadStateAdapter<NewsLoadStateAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val pb = itemView.findViewById<ProgressBar>(R.id.pbFooter)
        val tv = itemView.findViewById<TextView>(R.id.tvFooter)
        val btn = itemView.findViewById<Button>(R.id.btnFooter)

        init {
            btn.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState){
            pb.isVisible = loadState is LoadState.Loading
            btn.isVisible = loadState !is LoadState.Loading
            tv.isVisible = loadState !is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)

    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.load_state_footer,parent,false)
        return LoadStateViewHolder(view)
    }
}