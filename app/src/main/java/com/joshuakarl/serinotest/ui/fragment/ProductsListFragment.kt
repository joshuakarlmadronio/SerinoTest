package com.joshuakarl.serinotest.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.joshuakarl.serinotest.R
import com.joshuakarl.serinotest.databinding.FragmentProductsListBinding
import com.joshuakarl.serinotest.model.Resource
import com.joshuakarl.serinotest.ui.adapter.ProductsListAdapter
import com.joshuakarl.serinotest.viewmodel.ProductViewModel


class ProductsListFragment: Fragment() {
    private var _binding: FragmentProductsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<ProductViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        load()
    }

    private fun load() {
        viewModel.products.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    // Disable all Pagination while loading
                    updatePaginationButtons(skip = 0, limit = 0, total = 0)
                    binding.apply {
                        // Show loading progressbar
                        productsListLoading.visibility = View.VISIBLE
                        // Hide RecyclerView
                        productsListRv.visibility = View.GONE
                    }
                }
                is Resource.Success -> {
                    response.data?.let {
                        binding.apply {
                            // Hide Loading and Error GUI
                            productsListLoading.visibility = View.GONE
                            productsListError.visibility = View.GONE
                            productsListErrorTv.visibility = View.GONE
                            // RecyclerView
                            productsListRv.apply {
                                visibility = View.VISIBLE
                                layoutManager = LinearLayoutManager(context)
                                setHasFixedSize(true)
                                adapter = ProductsListAdapter(it.products)
                            }
                            // Pagination below
                            productsListPaginationLabel.text = context?.getString(
                                R.string.pagination_label, (it.skip + 1), (it.skip + it.limit), it.total)
                            updatePaginationButtons(it.skip, it.limit, it.total)
                        }
                    }
                }
                is Resource.Error -> {
                    // Disable all Pagination if error
                    updatePaginationButtons(skip = 0, limit = 0, total = 0)
                    binding.apply {
                        // Show error message
                        productsListError.visibility = View.VISIBLE
                        productsListErrorTv.visibility = View.VISIBLE
                        // Hide RecyclerView
                        productsListRv.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun updatePaginationButtons(skip: Int, limit: Int, total: Int) {
        binding.apply {
            productsListPaginationFirst.enable(skip > 0)
            productsListPaginationPrevious.enable(skip > 0)
            productsListPaginationLast.enable(skip + limit < total)
            productsListPaginationNext.enable(skip + limit < total)
        }
    }

    private fun ImageButton.enable(enable: Boolean = true) {
        this.isClickable = enable
        this.isEnabled = enable
        this.alpha = if (enable) 1.0f else 0.4f
    }
}