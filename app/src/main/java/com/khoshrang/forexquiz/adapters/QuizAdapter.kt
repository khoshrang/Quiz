package com.khoshrang.forexquiz.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.khoshrang.forexquiz.R
import com.khoshrang.forexquiz.data.Test
import com.khoshrang.forexquiz.data.SharedViewModel
import com.khoshrang.forexquiz.databinding.ItemQuizBinding
import com.khoshrang.forexquiz.ui.HomeFragmentDirections

/**
 * adapter for HomeFragment
 * prepares data of quizzes from assets csv
 */

class QuizAdapter(
    private val tests: List<Test>,
    val model: SharedViewModel,
    val context: Context,
    private val isPremium1: Boolean
) : RecyclerView.Adapter<QuizAdapter.ItemViewHolder>() {

    private var _binding: ItemQuizBinding? = null
    val binding: ItemQuizBinding get() = _binding!!

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val testName1 = binding.testName
        private val testGrade = binding.testGrade
        private val testImg = binding.testImg
        private val lockImg = binding.lockImg
        private val testCard = binding.testCard

        fun bind(test: Test, position: Int, isPremium: Boolean) {

            testName1.text = makeTestName(position)

            testGrade.text = makeGrade(test.test_grade)
            testImg.setImageResource(test.test_img)

            lockImg.visibility = View.GONE

            if (!isPremium) {
                if (position + 1 > 3) {
                    lockImg.visibility = View.VISIBLE
                } else {
                    lockImg.visibility = View.GONE
                }

            } else {
                lockImg.visibility = View.GONE
            }

            testCard.setOnClickListener {

                if (!isPremium && position + 1 > 3) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.lockedItems),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val action = HomeFragmentDirections.actionNavigationHomeToTestFragment(test.test_name)
                itemView.findNavController().navigate(action)
            }
        }
    }
    /**
     * makeGrade creates a grade with %
     * @return testGrade
     */
    private fun makeGrade(grade: String):String {
        val testGrade = buildString {
            append(grade)
            append(" ")
            append("%")
        }
        return testGrade
    }

    /**
     * makeTestName creates a name base on position of an Item
     * @return testName
     */
    private fun makeTestName(position: Int): String {
        val testName = buildString {
            append(context.getString(R.string.testMainName))
            append(" ")
            append(position + 1)
        }
        return testName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        _binding = ItemQuizBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val test = tests[position]
        holder.bind(test, position, isPremium1)
    }

    override fun getItemCount(): Int {
        return tests.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}