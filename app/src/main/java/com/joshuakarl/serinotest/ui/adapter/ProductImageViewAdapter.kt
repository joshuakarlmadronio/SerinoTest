package com.joshuakarl.serinotest.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joshuakarl.serinotest.databinding.ProductImageBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class ProductImageViewAdapter(private val images: List<Uri>)
    : RecyclerView.Adapter<ProductImageViewAdapter.ProductImageViewHolder>()
{
    inner class ProductImageViewHolder(private val binding: ProductImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Uri) {
            val callback = object: Callback {
                override fun onSuccess() {
                    binding.productImageLoading.visibility = View.GONE
                }
                override fun onError(e: Exception?) {
                    e?.printStackTrace()
                }
            }
            Picasso.get()
                .load(image)
                .into(binding.productImageIv, callback)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImageViewHolder {
        val binding = ProductImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductImageViewHolder, position: Int) {
        val image = images[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int = images.size
}