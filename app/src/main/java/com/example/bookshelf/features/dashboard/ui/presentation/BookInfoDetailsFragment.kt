package com.example.bookshelf.features.dashboard.ui.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.bookshelf.R
import com.example.bookshelf.databinding.FragmentBookInfoDetailsBinding
import com.example.bookshelf.features.common.local.BookShelfDataStoreManager
import com.example.bookshelf.features.dashboard.data.model.BookInfo
import com.example.bookshelf.features.dashboard.data.model.TagsInfo
import com.example.bookshelf.features.dashboard.ui.viewmodel.DashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookInfoDetailsFragment : Fragment() {
    private var _viewBinding: FragmentBookInfoDetailsBinding? = null
    private val viewBinding: FragmentBookInfoDetailsBinding get() = _viewBinding!!

    private val mViewModel: DashViewModel by viewModels()
    private var mBookInfo: BookInfo? = null
    private var mEmail = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentBookInfoDetailsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mEmail = BookShelfDataStoreManager.get(requireContext()).getCurrentUser()
        mBookInfo = arguments?.getParcelable(BOOK_INFO)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = mBookInfo?.title
        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            mViewModel.mTagInfo
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { tagInfo ->
                    updateUI(tagInfo)
                }
        }

        viewBinding.addTagImageView.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun updateUI(tagItems: List<String>) {
        mBookInfo?.let { bookInfo ->
            with(viewBinding) {
                titleTextView.text = bookInfo.title
                scoreTextView.text = bookInfo.score.toString()
                publishedDateTextView.text = bookInfo.publishedChapterDate.toString()
                Glide.with(requireContext()).load(bookInfo.image).into(animeImageView)

                val layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                tagRecyclerView.layoutManager = layoutManager

                val adapter = TagInfoAdapter(tagItems)
                tagRecyclerView.adapter = adapter
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mBookInfo?.id?.let { mViewModel.getTagsList(mEmail, it) }
    }

    private fun showAlertDialog() {
        val activity = requireActivity()
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(activity.getString(R.string.add_your_tag))

        val input = EditText(requireContext())
        builder.setView(input)

        builder.setPositiveButton(activity.getString(R.string.add)) { dialog, _ ->
            val userInput = input.text.toString()
            lifecycleScope.launch {
                val success = mViewModel.insertTags(
                    TagsInfo(
                        email = mEmail,
                        bookId = mBookInfo?.id,
                        tag = userInput
                    )
                )
                if (success) {
                    mBookInfo?.id?.let { mViewModel.getTagsList(mEmail, it) }
                }
            }
            dialog.dismiss()
        }
        builder.setNegativeButton(activity.getString(R.string.dismiss)) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    companion object {
        const val BOOK_INFO = "BOOK_INFO"
    }
}