package com.joshuakarl.serinotest.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.joshuakarl.serinotest.R
import com.joshuakarl.serinotest.databinding.ProductPreviewBinding
import com.joshuakarl.serinotest.model.Product
import com.joshuakarl.serinotest.ui.fragment.ProductsListFragmentDirections
import com.joshuakarl.serinotest.util.NumberFormatter
import com.squareup.picasso.Picasso

class ProductsListAdapter(private val products: List<Product>)
    : RecyclerView.Adapter<ProductsListAdapter.ViewHolder>()
{
    inner class ViewHolder(private val binding: ProductPreviewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val product = products[position]

            binding.root.setOnClickListener {
                showProductDetails(position)
            }

            binding.apply {
                productPreviewTitle.text = product.title
                productPreviewDetails.text = product.description
                productPreviewPrice.text = NumberFormatter.getCurrencyString(product.price)
                Picasso.get()
                    .load(product.thumbnail)
                    .centerCrop()
                    .resizeDimen(R.dimen.product_preview_size, R.dimen.product_preview_size)
                    .into(productPreviewIv)
            }
        }

        private fun showProductDetails(position: Int) {
            val action = ProductsListFragmentDirections.actionProductsListFragmentToProductDetailFragment(position)
            binding.root.findNavController().navigate(action)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = products.size
}