package com.example.bookshelf.features.dashboard.ui.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookshelf.databinding.ItemPublishedDateBinding

class PublishedDateAdapter(
    private val booksByDate: List<Long>,
    private var onItemClicked: ((book: Long) -> Unit)
) : RecyclerView.Adapter<PublishedDateAdapter.PublishedDateAdapterViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PublishedDateAdapterViewHolder {
        val binding =
            ItemPublishedDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PublishedDateAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int = booksByDate.size

    override fun onBindViewHolder(holder: PublishedDateAdapterViewHolder, position: Int) {
        if (position == 0) {
            onItemClicked(booksByDate[0])
        }
        holder.bind(booksByDate[position])
    }

    inner class PublishedDateAdapterViewHolder(private val binding: ItemPublishedDateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(bookByDate: Long) = binding.apply {
            yearTextView.text = bookByDate.toString()

            root.setOnClickListener {
                onItemClicked(bookByDate)
            }
        }
    }
}