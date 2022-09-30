package com.khoshrang.forexquiz.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.khoshrang.forexquiz.data.Book
import com.khoshrang.forexquiz.R
import com.khoshrang.forexquiz.adapters.BookAdapter
import com.khoshrang.forexquiz.databinding.FragmentEducationBinding
import com.khoshrang.forexquiz.data.SharedViewModel

class EducationFragment : Fragment() {

    private var _binding: FragmentEducationBinding? = null
    private val binding get() = _binding!!
    private val model: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEducationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val bookList = listOf<Book>(
            Book(getString(R.string.Chapter1), getString(R.string.Chapter1Pages), 1),
            Book(getString(R.string.secondChapter), getString(R.string.secondChapterPages), 2),
            Book(getString(R.string.thirdChapter), getString(R.string.thirdChapterPages), 3),
            Book(getString(R.string.forthChapter), getString(R.string.forthChapterStrings), 4),
            Book(getString(R.string.fifthChapter), getString(R.string.fifthChapterPages), 5),
            Book(getString(R.string.sixChapter), getString(R.string.sixChapterPages), 6),
        )
        val adapter = BookAdapter(bookList, model)
        binding.bookRecycle.adapter = adapter

        val time2 = System.currentTimeMillis()
        if (time2 > 1664463689000L) {
            binding.bookRecycle.visibility = View.VISIBLE
            binding.bookComingSoon.visibility = View.GONE
        } else {
            binding.bookRecycle.visibility = View.GONE
            binding.bookComingSoon.visibility = View.VISIBLE

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}