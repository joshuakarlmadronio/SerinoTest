package com.joshuakarl.serinotest.ui.adapter

import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joshuakarl.serinotest.R
import com.joshuakarl.serinotest.databinding.ProductPreviewBinding
import com.joshuakarl.serinotest.model.Product
import com.squareup.picasso.Picasso

class ProductsListAdapter(private val products: List<Product>): RecyclerView.Adapter<ProductsListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ProductPreviewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                productPreviewTitle.text = product.title
                productPreviewDetails.text = product.description
                productPreviewPrice.text = getCurrencyString(product.price)
                Picasso.get()
                    .load(product.thumbnail)
                    .centerCrop()
                    .resizeDimen(R.dimen.product_preview_size, R.dimen.product_preview_size)
                    .into(productPreviewIv)
            }
        }

        private fun getCurrencyString(n: Float): String {
            val currencyFormat = NumberFormat.getCurrencyInstance()
            currencyFormat.maximumFractionDigits = 2
            return currencyFormat.format(n)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = products.size
}