package com.besirkaraoglu.newsapp.ui.favorites

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.besirkaraoglu.newsapp.R
import com.besirkaraoglu.newsapp.model.Article
import com.besirkaraoglu.newsapp.model.ArticleDbEntity
import com.besirkaraoglu.newsapp.model.Source
import com.besirkaraoglu.newsapp.ui.FavoritesFragmentDirections
import com.besirkaraoglu.newsapp.ui.NewsViewModel
import com.besirkaraoglu.newsapp.util.DataState
import com.besirkaraoglu.newsapp.util.RecyclerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites),
        FavoritesAdapter.OnItemClickListener {

    private val viewModel: NewsViewModel by viewModels()
    private lateinit var adapter : FavoritesAdapter
    private val ITEM_SPACE = 20

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = requireActivity().findViewById<RecyclerView>(R.id.recyclerFav)
        adapter = FavoritesAdapter(this)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        recycler.addItemDecoration(RecyclerItemDecoration(ITEM_SPACE))

        viewModel.favState.observe(viewLifecycleOwner){
            when(it){
                is DataState.Loading ->{}
                is DataState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Error getting favorites. Cause: ${it.e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is DataState.Success -> {
                    val list = it.data
                    Collections.reverse(list)
                    adapter.submitList(list)
                }
            }

        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavorites()
    }

    override fun onItemClicked(articleDbEntity: ArticleDbEntity) {
        val article = Article(source = Source(articleDbEntity.sourceId,articleDbEntity.sourceName),
            author = articleDbEntity.author,
            title = articleDbEntity.title,
            description = articleDbEntity.description,
            url = articleDbEntity.url,
            urlToImage = articleDbEntity.urlToImage,
            publishedAt = articleDbEntity.publishedAt,
            content = articleDbEntity.content
        )
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToDetailedNewsFragment(article)
        findNavController().navigate(action)
    }

    override fun onItemLongClicked(articleDbEntity: ArticleDbEntity,position: Int): Boolean {
        AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setTitle("DELETE ${articleDbEntity.title}")
            .setMessage("Are you sure you want to delete ?")
            .setPositiveButton("YES", DialogInterface.OnClickListener { dialog, which ->
                viewModel.delete(articleDbEntity)
                adapter.notifyItemRemoved(position)
            })
            .setNegativeButton("CANCEL",null)
            .create()
            .show()
        return true
    }


}
