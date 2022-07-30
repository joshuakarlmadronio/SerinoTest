package com.joshuakarl.serinotest.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.joshuakarl.serinotest.databinding.FragmentProductImageBinding
import com.squareup.picasso.Picasso

class ProductImageFragment(val image: Uri): Fragment() {
    private var _binding: FragmentProductImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            Picasso.get()
                .load(image)
                .centerCrop()
                .into(root)
        }
    }
}