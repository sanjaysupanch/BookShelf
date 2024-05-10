package com.example.bookshelf.features.dashboard.ui.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookshelf.databinding.ItemTagsInfoBinding

class TagInfoAdapter(
    private val tagList: List<String>
) : RecyclerView.Adapter<TagInfoAdapter.TagInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagInfoViewHolder {
        val binding = ItemTagsInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagInfoViewHolder(binding)
    }

    override fun getItemCount(): Int = tagList.size

    override fun onBindViewHolder(holder: TagInfoViewHolder, position: Int) {
        holder.bind(tagList[position])
    }

    inner class TagInfoViewHolder(private val binding: ItemTagsInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tag: String) = binding.apply {
            tagTextView.text = tag
        }
    }
}