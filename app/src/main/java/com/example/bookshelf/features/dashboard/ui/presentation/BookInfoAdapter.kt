package com.example.bookshelf.features.dashboard.ui.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookshelf.databinding.ItemBookInfoBinding
import com.example.bookshelf.features.dashboard.data.model.BookInfo

class BookInfoAdapter(
    private val books: List<BookInfo>,
    private var onItemClicked: ((bookInfo: BookInfo) -> Unit)
) : RecyclerView.Adapter<BookInfoAdapter.BookInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookInfoViewHolder {
        val binding =
            ItemBookInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookInfoViewHolder(binding)
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: BookInfoViewHolder, position: Int) {
        holder.bind(books[position])
    }

    inner class BookInfoViewHolder(private val binding: ItemBookInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(bookInfo: BookInfo) = binding.apply {
            titleTextView .text = bookInfo.title
            scoreTextView.text = bookInfo.score.toString()
            publishedDateTextView.text = "Published in ${bookInfo.publishedChapterDate}"
            Glide.with(itemView).load(bookInfo.image).into(animeImageView)

            root.setOnClickListener {
                onItemClicked(bookInfo)
            }
        }
    }
}