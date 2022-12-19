package com.realm.imade.ui.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import com.realm.imade.adapter.ListAdapter
import com.realm.imade.config.Config.FAVORITE_LIST
import com.realm.imade.config.Config.isOnline
import com.realm.imade.databinding.FragmentBaseBinding
import com.realm.imade.db.BaseList
import com.realm.imade.ui.model.PageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentBase : Fragment(), ListAdapter.ListAdapterInterface {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListAdapter
    private lateinit var progress: CircularProgressIndicator
    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentBaseBinding? = null
    private val binding get() = _binding!!
    private lateinit var contexts: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contexts = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this)[PageViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBaseBinding.inflate(inflater, container, false)
        val root = binding.root
        recyclerView = binding.recyclerView
        progress = binding.progressCircular
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResult(listItem: BaseList, int: Int) {

        pageViewModel.add(FAVORITE_LIST, listItem.key, listItem.image)
    }

    override fun onResume() {
        super.onResume()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        pageViewModel.subscribeBase()

        CoroutineScope(Dispatchers.IO).launch {
            if (!isOnline()) {
                run {
                    Snackbar.make(binding.root, "Error Connect", LENGTH_SHORT).show()
                }
            }
        }
        pageViewModel._baseInit.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    adapter = ListAdapter(pageViewModel._baseListMutable.value!!, requireActivity(), this)
                    adapter.notifyItemChanged(0)
                    recyclerView.adapter = adapter
                    progress.visibility = GONE
                }
                false -> {

                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pageViewModel._baseInit.removeObservers(requireActivity())
    }

    override fun onDestroy() {
        super.onDestroy()
        pageViewModel._baseInit.removeObservers(requireActivity())

    }


}