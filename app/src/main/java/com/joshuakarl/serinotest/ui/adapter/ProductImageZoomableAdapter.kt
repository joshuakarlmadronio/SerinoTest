package com.joshuakarl.serinotest.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.joshuakarl.serinotest.databinding.ProductImageZoomableBinding
import com.joshuakarl.serinotest.ui.fragment.ProductDetailFragmentDirections
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ProductImageZoomableAdapter(private val images: List<Uri>)
    : RecyclerView.Adapter<ProductImageZoomableAdapter.ProductImageZoomableViewHolder>()
{
    inner class ProductImageZoomableViewHolder(private val binding: ProductImageZoomableBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val image = images[position]

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

        private fun showProductImagesFullscreen(position: Int) {
            val uriStrings = images.map { it.toString() }.toTypedArray()
            val action = ProductDetailFragmentDirections.actionProductDetailFragmentToProductImageFragment(uriStrings, position)
            binding.root.findNavController().navigate(action)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductImageZoomableViewHolder {
        val binding = ProductImageZoomableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductImageZoomableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductImageZoomableViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = images.size
}