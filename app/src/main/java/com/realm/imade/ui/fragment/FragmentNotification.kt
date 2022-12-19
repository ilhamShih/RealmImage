package com.realm.imade.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.realm.imade.NotificationM.showNotification
import com.realm.imade.databinding.FragmentNotificationBinding


class FragmentNotification : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    lateinit var contexts: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contexts = context
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val root = binding.root
        val materialButton = binding.materialButton
        materialButton.setOnClickListener {
            contexts.showNotification("Title Image", "New Image")
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}