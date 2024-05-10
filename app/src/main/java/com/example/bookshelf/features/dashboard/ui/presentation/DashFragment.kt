package com.example.bookshelf.features.dashboard.ui.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookshelf.R
import com.example.bookshelf.databinding.FragmentDashBinding
import com.example.bookshelf.features.common.local.BookShelfDataStoreManager
import com.example.bookshelf.features.dashboard.data.model.BookInfo
import com.example.bookshelf.features.dashboard.ui.viewmodel.DashViewModel
import com.example.bookshelf.features.utils.UiStateResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@AndroidEntryPoint
class DashFragment : Fragment(), MenuProvider {
    private var _viewBinding: FragmentDashBinding? = null
    private val viewBinding: FragmentDashBinding get() = _viewBinding!!

    private val mViewModel: DashViewModel by viewModels()
    private var mBooksInfo : List<BookInfo> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentDashBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            requireActivity().getString(R.string.app_name)
    }

    override fun onResume() {
        super.onResume()
        observer()
    }

    private fun observer() {
        observeState()
        observeBookInfo()
    }

    private fun observeBookInfo() {
        if (mBooksInfo.isNotEmpty()) {
            updateUI(mBooksInfo)
        } else {
            lifecycleScope.launch {
                mViewModel.mBookList
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    .collect { result ->
                        result?.let { resultList ->
                            resultList.forEach { bookInfo ->
                                val updatedPublishedChapterDate =
                                    timestampToYears(bookInfo.publishedChapterDate)
                                bookInfo.publishedChapterDate = updatedPublishedChapterDate.toLong()
                            }
                            mBooksInfo = resultList
                            updateUI(resultList)
                        }
                            ?: requireActivity().showToast("Unable to fetch API response \uD83D\uDE40")
                    }
            }
        }
    }

    private fun updateUI(bookInfoList: List<BookInfo>) {
        val booksByDate = bookInfoList.groupBy { it.publishedChapterDate }.keys.sortedDescending()

        val horizontalLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewBinding.yearRecyclerView.layoutManager = horizontalLayoutManager

        val adapter = PublishedDateAdapter(booksByDate) { publishedChapterDate ->
            val books = bookInfoList.filter { it.publishedChapterDate == publishedChapterDate }

            // Create the vertical RecyclerView layout manager once
            val verticalLayoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            viewBinding.bookInfoRecyclerView.layoutManager = verticalLayoutManager

            // Create and set the adapter for the book info RecyclerView
            val adapterVertical = BookInfoAdapter(books) { bookInfo ->
                val bundle = Bundle()
                bundle.putParcelable(BOOK_INFO, bookInfo)
                findNavController().navigate(R.id.action_dashFragment_to_bookInfoDetailsFragment, bundle)
            }
            viewBinding.bookInfoRecyclerView.adapter = adapterVertical
        }
        viewBinding.yearRecyclerView.adapter = adapter
    }

    private fun timestampToYears(timestamp: Long?): Int {
        if (timestamp == null) return 0
        val publishedDate = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(timestamp),
            ZoneOffset.UTC
        )
        return publishedDate.year
    }

    private fun observeState() {
        mViewModel.mState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: UiStateResult) {
        when (state) {
            is UiStateResult.IsLoading -> handleLoading(state.isLoading)
            is UiStateResult.Init -> Unit
            is UiStateResult.Error -> requireActivity().showToast(state.message)
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            viewBinding.loading.visibility = View.VISIBLE
        } else {
            viewBinding.loading.visibility = View.GONE
        }
    }

    private fun Context.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_sign_out -> {
                BookShelfDataStoreManager.get(requireActivity()).clearSession()
                requireActivity().showToast(requireActivity().getString(R.string.sign_out_msg))
                findNavController().navigate(R.id.action_dashFragment_to_welcomeFragment)
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    companion object {
        const val BOOK_INFO = "BOOK_INFO"
    }
}