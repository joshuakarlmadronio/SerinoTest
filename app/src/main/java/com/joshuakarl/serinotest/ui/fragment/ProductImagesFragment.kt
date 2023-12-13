package com.joshuakarl.serinotest.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.joshuakarl.serinotest.databinding.FragmentProductImagesBinding
import com.joshuakarl.serinotest.ui.adapter.ProductImageZoomableAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductImagesFragment: Fragment() {
    private var _binding: FragmentProductImagesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uris = requireArguments().getStringArray(PRODUCT_URIS)?.map { uri -> Uri.parse(uri) }
        uris?.let {
            binding.productImagesVp.apply {
                adapter = ProductImageZoomableAdapter(uris)
                setOnClickListener {

                }
                post {
                    // Position of the image shown
                    currentItem = requireArguments().getInt(PRODUCT_POSITION)
                        .coerceIn(0, (uris.size - 1))
                }
            }
        }
        // Indicator
        TabLayoutMediator(binding.productImagesVpIndicator, binding.productImagesVp) { _, _ ->
        }.attach()
    }

    companion object {
        const val PRODUCT_URIS = "PRODUCT_URIS"
        const val PRODUCT_POSITION = "PRODUCT_POSITION"
    }
}