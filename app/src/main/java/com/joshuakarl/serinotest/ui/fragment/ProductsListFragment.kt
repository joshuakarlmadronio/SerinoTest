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

    data class Pagination(var skip: Int, var limit: Int = 10, var total: Int)
    private var pagination: Pagination? = null

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
        // Pagination listeners
        binding.apply {
            productsListPaginationFirst.setOnClickListener {
                pagination?.let {
                    viewModel.getProducts(0)
                    load()
                }
            }
            productsListPaginationNext.setOnClickListener {
                pagination?.let {
                    viewModel.getProducts(it.skip + it.limit)
                    load()
                }
            }
            productsListPaginationLast.setOnClickListener {
                pagination?.let {
                    viewModel.getProducts(it.total - it.limit)
                    load()
                }
            }
            productsListPaginationPrevious.setOnClickListener {
                pagination?.let {
                    viewModel.getProducts(it.skip - it.limit)
                    load()
                }
            }
        }

        load()
    }

    private fun load() {
        viewModel.products.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    // Disable all Pagination while loading
                    pagination = Pagination(0, 0, 0)
                    updatePaginationButtons()
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
                            pagination = Pagination(it.skip, it.limit, it.total)
                            updatePaginationButtons()
                        }
                    }
                }
                is Resource.Error -> {
                    // Disable all Pagination if error
                    pagination = Pagination(0, 0, 0)
                    updatePaginationButtons()
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

    private fun updatePaginationButtons() {
        binding.apply {
            productsListPaginationFirst.enable(pagination!!.skip > 0)
            productsListPaginationPrevious.enable(pagination!!.skip > 0)
            productsListPaginationLast.enable((pagination!!.skip + pagination!!.limit) < pagination!!.total)
            productsListPaginationNext.enable((pagination!!.skip + pagination!!.limit) < pagination!!.total)
        }
    }

    private fun ImageButton.enable(enable: Boolean = true) {
        this.isClickable = enable
        this.isEnabled = enable
        this.alpha = if (enable) 1.0f else 0.4f
    }
}