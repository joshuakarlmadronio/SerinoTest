package com.joshuakarl.serinotest.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.joshuakarl.serinotest.R
import com.joshuakarl.serinotest.databinding.FragmentProductDetailBinding
import com.joshuakarl.serinotest.model.Resource
import com.joshuakarl.serinotest.ui.adapter.ProductImageViewAdapter
import com.joshuakarl.serinotest.util.NumberFormatter
import com.joshuakarl.serinotest.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment: Fragment() {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val args: ProductDetailFragmentArgs by navArgs()
    private val viewModel by activityViewModels<ProductViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.products.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        val product = it.products[args.index]
                        binding.apply {
                            productDetailTitle.text = product.title
                            productDetailPrice.text = NumberFormatter.getCurrencyString(product.price)
                            productDetailRating.text = NumberFormatter.getRatingString(product.rating)
                            productDetailStock.text = getString(R.string.x_left_in_stock, product.stock)
                            productDetailCategory.text = getString(R.string.in_category_brand, product.category, product.brand)
                            productDetailDescription.text = product.description
                            productDetailImagesVp.adapter = ProductImageViewAdapter(product.images)
                        }

                        // Indicator
                        TabLayoutMediator(binding.productDetailImagesVpIndicator, binding.productDetailImagesVp) { _, _ ->
                        }.attach()
                    }
                }
                else -> {
                    Log.e(TAG, resource.message ?: "Resource error")
                }
            }
        }
    }

    companion object {
        private const val TAG = "ProductDetailFragment"
    }
}