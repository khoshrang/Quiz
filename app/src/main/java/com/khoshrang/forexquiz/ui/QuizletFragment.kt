package com.khoshrang.forexquiz.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.khoshrang.forexquiz.data.Question
import com.khoshrang.forexquiz.R
import com.khoshrang.forexquiz.adapters.QuizletAdapter
import com.khoshrang.forexquiz.data.SharedViewModel
import com.khoshrang.forexquiz.databinding.FragmentQuizletBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.util.*

class QuizletFragment : Fragment() {

    var questionList: ArrayList<Question> = ArrayList<Question>()
    private val navigationArgs: QuizletFragmentArgs by navArgs()
    private val model: SharedViewModel by activityViewModels()
    private var _binding: FragmentQuizletBinding? = null
    private val binding get() = _binding!!
    var dialogText :String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        _binding = FragmentQuizletBinding.inflate(inflater, container, false)
        val root: View = binding.root

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    showDialog(dialogText)

                }
            })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewpager: ViewPager2 = binding.viewPager
        var adapter = QuizletAdapter(questionList, model, navigationArgs.isResult)
        viewpager.adapter = adapter
        val filename = navigationArgs.testName

        if (navigationArgs.isResult) {
            questionList = model.questionList
            binding.finishBtn.text = context?.getString(R.string.Exit)
            dialogText = getString(R.string.exitTest)
        } else {
            readAndInsert(filename)
            questionList.shuffle()
            model.questionList = questionList
            dialogText = getString(R.string.exitTestToResult)

        }

        adapter = QuizletAdapter(questionList, model, navigationArgs.isResult)
        viewpager.adapter = adapter

        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                model.currentQuestionNumber.value = position + 1
            }

        })

        binding.finishBtn.setOnClickListener {
            showDialog(dialogText)
        }
        binding.questionNumberTxt.text = buildString {
            append("1")
            append(" / ")
            append(questionList.size)

        }
        val pageObserver = Observer<Int> {
            binding.questionNumberTxt.text = buildString {
                append(it)
                append(" / ")
                append(questionList.size)

            }
            val progress = ((it.toDouble() / questionList.size) * 100).toInt()
            binding.progressTest.progress = progress

        }
        model.currentQuestionNumber.observe(viewLifecycleOwner, pageObserver)

    }

    private fun showDialog(text: String?) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.attention))
        builder.setMessage(text)

        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            if (navigationArgs.isResult) {
                val action = QuizletFragmentDirections.actionTestFragmentToNavigationHome()
                findNavController().navigate(action)

            } else {
                val action =
                    QuizletFragmentDirections.actionTestFragmentToResultFragment(testName = navigationArgs.testName)
                findNavController().navigate(action)

            }
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ ->
        }
        builder.show()
    }

    @Throws(UnsupportedEncodingException::class)
    private fun readAndInsert(fileName: String) {
        questionList.clear()
        var `is`: InputStreamReader? = null
        try {
            `is` = InputStreamReader(
                requireContext().assets
                    .open(fileName)
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val reader = BufferedReader(`is`)
        try {
            reader.forEachLine {
                val rowData = it.split(",").toTypedArray()
                if (rowData.size > 3) {
                    val question1 = rowData[0].trim()
                    val testId = rowData[1].trim()
                    val op1 = getString(R.string.QindexA) +" "+ rowData[2].trim()
                    val op2 = getString(R.string.QindexB) +" "+ rowData[3].trim()
                    val op3 = getString(R.string.QindexC) +" "+ rowData[4].trim()
                    val op4 = getString(R.string.QindexD) +" "+ rowData[5].trim()
                    val answer = rowData[6].trim()
                    val rowNumber = rowData[7].trim()
                    val rowString = rowNumber.trim { it <= ' ' }.replace("[^0-9]".toRegex(), "")
                    if (rowString.isEmpty()) return@forEachLine

                    val question = Question(
                        question1,
                        answer,
                        testId,
                        op1,
                        op2,
                        op3,
                        op4,
                        "0"
                    )
                    questionList.add(question)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}