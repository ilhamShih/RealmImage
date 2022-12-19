package com.realm.imade.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.realm.imade.adapter.ListAdapter
import com.realm.imade.databinding.FragmentFavoriteBinding
import com.realm.imade.db.BaseList
import com.realm.imade.ui.model.PageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FragmentFavorite : Fragment(), ListAdapter.ListAdapterInterface {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ListAdapter
    lateinit var progress: CircularProgressIndicator
    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private var mList: ArrayList<BaseList> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this)[PageViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root = binding.root
        recyclerView = binding.recyclerView
        progress = binding.progressCircular
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        pageViewModel.subscribeFavorites()


        return root
    }

    override fun onStart() {
        super.onStart()
        pageViewModel._favoriteInit.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    mList = pageViewModel._favoritesListMutable.value!!
                    adapter = activity?.let { it1 -> ListAdapter(mList, it1, this) }!!
                    recyclerView.adapter = adapter
                    progress.visibility = GONE
                }
                false -> {
                    progress.visibility = GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        pageViewModel._favoriteInit.removeObservers(requireActivity())

    }

    override fun onResult(listItem: BaseList, int: Int) {
        pageViewModel.deliteEntry(listItem.key)
        CoroutineScope(Dispatchers.IO).launch {
            progress.post {
                progress.visibility = VISIBLE
            }
            delay(500)
            progress.post {
                progress.visibility = GONE
            }
            mList.removeAt(int)
            adapter.notifyItemRemoved(int)
        }
    }
}