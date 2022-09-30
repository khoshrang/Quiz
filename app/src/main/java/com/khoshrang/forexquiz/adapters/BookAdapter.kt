package com.khoshrang.forexquiz.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.khoshrang.forexquiz.data.Book
import com.khoshrang.forexquiz.R
import com.khoshrang.forexquiz.data.SharedViewModel
import com.khoshrang.forexquiz.databinding.ItemBookBinding
import com.khoshrang.forexquiz.ui.EducationFragmentDirections

/**
 * adapter for Education Fragment
 * @property books is list of books in assets
 */

class BookAdapter(private val books: List<Book>, val model: SharedViewModel) :
    RecyclerView.Adapter<BookAdapter.ItemViewHolder>() {

    private var _binding: ItemBookBinding? = null
    val binding: ItemBookBinding get() = _binding!!

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(book: Book) {
            binding.bookName.text = book.book_name
            binding.bookPages.text = book.book_pages
            val img = binding.sportsImage

            when (book.id) {
                1 -> img.setImageResource(R.drawable.test_4)
                2 -> img.setImageResource(R.drawable.test_5)
                3 -> img.setImageResource(R.drawable.test_1)
                4 -> img.setImageResource(R.drawable.test_8)
                5 -> img.setImageResource(R.drawable.test_9)
                6 -> img.setImageResource(R.drawable.test_2)
            }

            binding.bookCard.setOnClickListener {
                val action = EducationFragmentDirections.actionNavigationDashboardToBookFragment(book.id)

                itemView.findNavController().navigate(action)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        _binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val test = books[position]
        holder.bind(test)
    }

    override fun getItemCount(): Int {
        return books.size
    }


}