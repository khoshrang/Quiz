package com.khoshrang.forexquiz.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.khoshrang.forexquiz.data.DataStoreManager
import com.khoshrang.forexquiz.R
import com.khoshrang.forexquiz.data.SharedViewModel
import com.khoshrang.forexquiz.databinding.FragmentResultBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    val binding: FragmentResultBinding get() = _binding!!
    private val model: SharedViewModel by activityViewModels()
    private val navArgs: ResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false)

        val questionList = model.questionList
        var right = 0
        var wrong = 0
        var noAnswer = 0

        for (question in questionList) {

            if (question.lastanswer == "0") {
                noAnswer++
            } else if (question.answer == question.lastanswer) {
                right++
            } else {
                wrong++
            }
        }

        val grade = (right.toDouble() / (questionList.size)) * 100

        lifecycleScope.launch(Dispatchers.IO) {
            val dataStoreManager = context?.let { DataStoreManager(it) }

            dataStoreManager?.saveGrade(
                navArgs.testName,
                grade.toInt().toString()
            )
        }


        binding.txtRightNum.text = buildString {
            append(right.toString())
            append(" ")
            append(getString(R.string.rightAnswer))
        }

        binding.txtWrongNum.text = buildString {
            append(wrong.toString())
            append(" ")
            append(getString(R.string.wrongAnswer))
        }

        binding.txtNoanswerNum.text = buildString {
            append(noAnswer.toString())
            append(" ")
            append(getString(R.string.withoutAnswer))
        }

        binding.txtGrade.text = buildString {
            append(grade.toInt().toString())
            append(" ")
            append(getString(R.string.percent))
        }

        if (grade.toInt() > 80) {
            binding.imageView.setImageResource(R.drawable.exam_good)
            binding.txtGrade.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            binding.txtExplain.text = getString(R.string.passText)
            binding.txtExplain.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        } else {
            binding.imageView.setImageResource(R.drawable.exam_bad)
            binding.txtGrade.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            binding.txtExplain.text = getString(R.string.failText)
            binding.txtExplain.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        }

        binding.btnResult.setOnClickListener {
            val action =
                ResultFragmentDirections.actionResultFragmentToTestFragment(isResult = true)
            findNavController().navigate(action)

        }

        binding.btnAgain.setOnClickListener {
            val action =
                ResultFragmentDirections.actionResultFragmentToTestFragment(navArgs.testName)
            findNavController().navigate(action)

        }

        binding.btnExit.setOnClickListener {

            findNavController().navigateUp()

        }

        return binding.root
    }

}