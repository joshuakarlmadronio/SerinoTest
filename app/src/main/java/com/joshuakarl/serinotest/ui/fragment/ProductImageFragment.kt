package com.joshuakarl.serinotest.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.joshuakarl.serinotest.R
import com.joshuakarl.serinotest.databinding.FragmentProductImageBinding
import com.joshuakarl.serinotest.model.Product
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductImageFragment: Fragment() {
    private var _binding: FragmentProductImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductImageBinding.inflate(inflater, container, false)
        val uri = Uri.parse(arguments?.getString("IMAGE_URI") ?: "")
        binding.root.setImageURI(uri)
        return binding.root
    }

    companion object {
        fun create(image: Uri): ProductImageFragment {
            val fragment = ProductImageFragment()
            val args = Bundle(1)
            args.putString("IMAGE_URI", image.toString())
            fragment.arguments = args
            return fragment
        }
    }
}