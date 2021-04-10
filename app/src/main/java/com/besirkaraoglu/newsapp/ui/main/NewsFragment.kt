package com.besirkaraoglu.newsapp.ui.main

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.besirkaraoglu.newsapp.R
import com.besirkaraoglu.newsapp.model.Article
import com.besirkaraoglu.newsapp.ui.NewsFragmentDirections
import com.besirkaraoglu.newsapp.ui.NewsViewModel
import com.besirkaraoglu.newsapp.util.RecyclerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news),
        NewsAdapter.OnItemClickListener {

    private val viewModel: NewsViewModel by viewModels()
    private val ITEM_SPACE = 20
    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etSearch = requireActivity().findViewById<EditText>(R.id.etSearch)
        val ibCancel = requireActivity().findViewById<ImageButton>(R.id.ibCancel)
        val recyclerView = requireActivity().findViewById<RecyclerView>(R.id.recycler)
        val pb = requireActivity().findViewById<ProgressBar>(R.id.pb)
        val tvError = requireActivity().findViewById<TextView>(R.id.tvError)
        val btnError = requireActivity().findViewById<Button>(R.id.btnError)
        val adapter = NewsAdapter(this)
        recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = NewsLoadStateAdapter { adapter.retry() },
                footer = NewsLoadStateAdapter { adapter.retry() }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(RecyclerItemDecoration(ITEM_SPACE))

        btnError.setOnClickListener {
            adapter.retry()
        }

        adapter.addLoadStateListener { loadState->
            if (loadState.refresh is LoadState.Loading){
                pb.visibility = View.VISIBLE
                tvError.visibility = View.GONE
                btnError.visibility = View.GONE
            }else{
                pb.visibility = View.GONE
            }

            val error = when{
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }


            error?.let {
                Toast.makeText(requireContext(), "Error! ${it.error.message}", Toast.LENGTH_SHORT).show()
                if (error == loadState.refresh){
                    tvError.visibility = View.VISIBLE
                    btnError.visibility = View.VISIBLE
                }
            }
        }

        etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (ibCancel.visibility == View.GONE){
                    ibCancel.visibility = View.VISIBLE
                }
                val query = etSearch.text.toString()
                if (query.isNotEmpty()){
                    job?.cancel()
                    job = lifecycleScope.launch {
                        viewModel.getSearchResults(etSearch.text.toString())
                            .collectLatest {
                                adapter.submitData(it)
                            }
                    }
                }else{
                    ibCancel.visibility = View.GONE
                    job?.cancel()
                }
            }

        })


        ibCancel.setOnClickListener {
            etSearch.text.clear()
            val im = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (requireActivity().currentFocus != null){
                im.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
            ibCancel.visibility = View.GONE
        }
    }

    override fun onItemClicked(article: Article) {
        val action = NewsFragmentDirections.actionNewsFragmentToDetailedNewsFragment(article)
        findNavController().navigate(action)
    }

}

