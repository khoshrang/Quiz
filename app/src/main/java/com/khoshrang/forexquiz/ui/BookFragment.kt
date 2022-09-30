package com.khoshrang.forexquiz.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.khoshrang.forexquiz.databinding.FragmentBookBinding

class BookFragment : Fragment() {

    private var _binding: FragmentBookBinding? = null
    val binding: FragmentBookBinding get() = _binding!!

    private val navArgs: BookFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        waite(2000L)
        val bookName = getBookName()
        binding.activityMainPdfView.fromAsset(bookName).show()

    }

    /**
     * waite , shows a progressbar at top of page
     * @param time is the duration of show
     */
    private fun waite(time: Long) {
        val progress = binding.progressTest
        progress.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            progress.visibility = View.GONE
        }, time)
    }

    /**
     * getBookName builds the book's name
     * @return book name from assets
     */
    private fun getBookName(): String {
        val bookId = navArgs.bookid
        val bookName = buildString {
            append("book")
            append(bookId)
            append(".pdf")
        }
        return bookName
    }

}