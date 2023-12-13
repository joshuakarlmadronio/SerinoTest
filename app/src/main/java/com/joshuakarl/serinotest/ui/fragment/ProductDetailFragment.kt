package com.joshuakarl.serinotest.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.navigation.fragment.navArgs
import com.joshuakarl.serinotest.R
import com.joshuakarl.serinotest.databinding.FragmentProductDetailBinding
import com.joshuakarl.serinotest.model.Resource
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

                        // Product images viewpager fragment
                        if (savedInstanceState == null) {
                            val uriStrings = product.images.map { uri -> uri.toString() }.toTypedArray()
                            val bundle = bundleOf(ProductImagesFragment.PRODUCT_URIS to uriStrings)
                            childFragmentManager.commit {
                                setReorderingAllowed(true)
                                add(R.id.product_detail_images_fragment, ProductImagesFragment::class.java, bundle)
                            }
                        }

                        binding.apply {
                            productDetailTitle.text = product.title
                            productDetailPrice.text = NumberFormatter.getCurrencyString(product.price)
                            productDetailRating.text = NumberFormatter.getRatingString(product.rating)
                            productDetailStock.text = getString(R.string.x_left_in_stock, product.stock)
                            productDetailCategory.text = getString(R.string.in_category_brand, product.category, product.brand)
                            productDetailDescription.text = product.description
                        }
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