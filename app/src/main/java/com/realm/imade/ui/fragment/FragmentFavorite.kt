package com.realm.imade.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.realm.imade.databinding.FragmentFavoriteBinding
import com.realm.imade.db.ItemFavoritesKey
import com.realm.imade.ui.model.PageViewModel
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentFavorite : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var listView: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(requireActivity())[PageViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root = binding.root
        listView = binding.listView

        CoroutineScope(Dispatchers.Main).launch {
            pageViewModel.subscribeFavorites()
        }
        pageViewModel.changesResults2.observe(requireActivity()) {
            when (it) {
                true -> {
                    pageViewModel.changesResults.value?.let { it1 ->
                        getFavoritList(it1)
                    }
                }
                false -> {
                    listView.removeAllViews()
                    pageViewModel.changesResults.value?.let { it1 ->
                        getFavoritList(it1)
                    }
                }
            }
        }
        return root
    }

    fun getFavoritList(resultsChange: ResultsChange<ItemFavoritesKey>) {
        resultsChange.list.forEach { itForList ->
            val imageView = ImageView(activity)
            activity?.let { it1 ->
                Glide.with(it1)
                    .load(itForList.items?.imageUrlFavorit)
                    .fitCenter().into(imageView)
            }
            listView.addView(imageView)
            imageView.setOnClickListener {
                pageViewModel.deliteEntry(itForList.key)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}