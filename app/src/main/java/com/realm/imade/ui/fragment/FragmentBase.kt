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
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import com.realm.imade.config.Config.FAVORITE_LIST
import com.realm.imade.config.Config.isOnline
import com.realm.imade.databinding.FragmentBaseBinding
import com.realm.imade.db.ItemBase
import com.realm.imade.ui.model.PageViewModel
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class FragmentBase : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentBaseBinding? = null
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

        _binding = FragmentBaseBinding.inflate(inflater, container, false)
        val root = binding.root
        listView = binding.listView

        CoroutineScope(Dispatchers.IO).launch {
            val isOnline = isOnline()
            async(Dispatchers.Main) {
                pageViewModel.errorConnect.value = isOnline
                if (pageViewModel.errorConnect.value == true) {
                    pageViewModel.getImage()
                } else {
                    run {
                        Snackbar.make(binding.root, "Error Connect", LENGTH_SHORT).show()
                    }
                }
            }
        }
        pageViewModel.subscribeBase()
        return root
    }

    override fun onStart() {
        super.onStart()
        pageViewModel.changesResults2Base.observe(requireActivity()) {
            when (it) {
                true -> {
                    pageViewModel.changesResultsBase.value?.let { it1 ->
                        getBaseList(it1)
                    }
                }
                false -> {
                    listView.removeAllViews()
                    pageViewModel.changesResultsBase.value?.let { it1 ->
                        getBaseList(it1)
                    }
                }
            }
        }
    }

    fun getBaseList(resultsChange: ResultsChange<ItemBase>) {
        resultsChange.list.forEach { itForList ->
            val imageView = ImageView(activity)
            activity?.let { it1 ->
                Glide.with(it1)
                    .load(itForList.imageUrl)
                    .fitCenter().into(imageView)
            }
            listView.addView(imageView)
            imageView.setOnClickListener {
                imageView.post {
                    pageViewModel.add(FAVORITE_LIST, itForList.key, itForList.imageUrl)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}