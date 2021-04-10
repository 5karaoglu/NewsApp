package com.besirkaraoglu.newsapp.ui.detailed

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.besirkaraoglu.newsapp.R
import com.besirkaraoglu.newsapp.ui.DetailedNewsFragmentArgs
import com.besirkaraoglu.newsapp.ui.NewsViewModel
import com.besirkaraoglu.newsapp.util.Functions
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailedNewsFragment : Fragment(R.layout.fragment_detailed_news) {

    private val viewModel: NewsViewModel by viewModels()
    private val args: DetailedNewsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val ibBack = requireActivity().findViewById<ImageButton>(R.id.ibBack)
        ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
        val buttonSource = requireActivity().findViewById<Button>(R.id.buttonSource)
        buttonSource.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url", args.article.url)
            findNavController().navigate(R.id.action_detailedNewsFragment_to_webViewFragment,bundle)
        }
        val ibFav = requireActivity().findViewById<ImageButton>(R.id.ibFavorites)
        ibFav.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Do you want to add this article to your favorites ?")
                .setPositiveButton("Yes"){
                    _,_ -> viewModel.insert(args.article) }
                .setNegativeButton("Cancel",null)
                .setCancelable(false)
                .create()
                .show()
        }
        val ibShare = requireActivity().findViewById<ImageButton>(R.id.ibShare)
        ibShare.setOnClickListener {
            val share = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, args.article.url)
                type = "text/plain"
            }, null)
            startActivity(share)
        }
        val iv = requireActivity().findViewById<ImageView>(R.id.ivDeatiled)
        val tvTitle = requireActivity().findViewById<TextView>(R.id.tvDetailedTitle)
        val tvContent = requireActivity().findViewById<TextView>(R.id.tvDetailedContent)
        val tvAuthor = requireActivity().findViewById<TextView>(R.id.tvDetailedAuthor)
        val tvDate = requireActivity().findViewById<TextView>(R.id.tvDetailedDate)

        Picasso.get()
            .load(args.article.urlToImage)
            .error(R.drawable.ic_baseline_cancel_24)
            .into(iv)
        tvTitle.text = args.article.title
        tvContent.text = args.article.content
        tvAuthor.text = args.article.author
        tvDate.text = Functions.formatDate(args.article.publishedAt)

    }

}