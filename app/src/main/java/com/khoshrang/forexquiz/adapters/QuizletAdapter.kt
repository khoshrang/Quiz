package com.khoshrang.forexquiz.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.khoshrang.forexquiz.R
import com.khoshrang.forexquiz.data.Question
import com.khoshrang.forexquiz.data.SharedViewModel
import com.khoshrang.forexquiz.databinding.ItemTestPagerBinding

/**
 * adapter for QuizletFragment
 * prepares tests for viewPager
 */

class QuizletAdapter(
    val questionList: ArrayList<Question>, private val model: SharedViewModel, val isResult: Boolean
) : RecyclerView.Adapter<QuizletAdapter.ViewPagerViewHolder>() {

    private var _binding: ItemTestPagerBinding? = null
    val binding: ItemTestPagerBinding get() = _binding!!

    /**
     * colorStateList will change the default color of radioButtons text from gray to black
     */
    val colorStateList = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_enabled),
        ), intArrayOf(
            Color.BLACK,  //disabled
        )
    )

    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val radio1 = binding.answer1Btn
        private val radio2 = binding.answer2Btn
        private val radio3 = binding.answer3Btn
        private val radio4 = binding.answer4Btn
        private val radioGroup = binding.radioAnswers
        private val img = binding.questionImg
        private val questionTxt = binding.questionTxt


        fun bind(questionSample: Question, position: Int) {

            questionTxt.text = questionSample.question.trim().replace("\"","")
            radio1.text = questionSample.option1.trim().replace("\"","")
            radio2.text = questionSample.option2.trim().replace("\"","")
            radio3.text = questionSample.option3.trim().replace("\"","")
            radio4.text = questionSample.option4.trim().replace("\"","")

            val testName = questionSample.testId.trim()
            setUserLastSelectedAnswer(questionSample)
            activateResultMode(questionSample)
            setQuestionRelatedImage(testName)

            radioGroup.setOnCheckedChangeListener { _, i ->

                setNewLastAnswer(i, questionSample)
                model.questionList = questionList
                compareLastAnswerWithTrueAnswer(questionSample)

            }
        }

        /**
         * compare the user selected answer with actual one
         * set the background on the actual answer
         */
        private fun compareLastAnswerWithTrueAnswer(questionSample: Question) {
            if (questionSample.lastanswer != "0") {
                when (questionSample.answer) {

                    "1" -> {
                        radio1.background =
                            (ContextCompat.getDrawable(itemView.context, R.drawable.border))
                        removeTrueBackgroundFromOtherOptions(
                            "2",
                            "3",
                            "4",
                            radio1,
                            radio2,
                            radio3,
                            radio4
                        )
                    }
                    "2" -> {
                        radio2.background =
                            (ContextCompat.getDrawable(itemView.context, R.drawable.border))
                        removeTrueBackgroundFromOtherOptions(
                            "1",
                            "3",
                            "4",
                            radio1,
                            radio2,
                            radio3,
                            radio4
                        )
                    }
                    "3" -> {
                        radio3.background =
                            (ContextCompat.getDrawable(itemView.context, R.drawable.border))
                        removeTrueBackgroundFromOtherOptions(
                            "2",
                            "1",
                            "4",
                            radio1,
                            radio2,
                            radio3,
                            radio4
                        )
                    }
                    "4" -> {
                        radio4.background =
                            (ContextCompat.getDrawable(itemView.context, R.drawable.border))
                        removeTrueBackgroundFromOtherOptions(
                            "2",
                            "3",
                            "1",
                            radio1,
                            radio2,
                            radio3,
                            radio4
                        )
                    }
                }
            }
        }

        /**
         * update user selected answer
         */
        private fun setNewLastAnswer(i: Int, questionSample: Question) {
            when (i) {
                R.id.answer1_btn -> {
                    questionSample.lastanswer = "1"
                }
                R.id.answer2_btn -> {
                    questionSample.lastanswer = "2"
                }
                R.id.answer3_btn -> {
                    questionSample.lastanswer = "3"
                }
                R.id.answer4_btn -> {
                    questionSample.lastanswer = "4"
                }
            }
        }

        private fun setQuestionRelatedImage(testName: String) {
            val checkExistence: Int = itemView.context.resources
                .getIdentifier(testName.trim(), "drawable", itemView.context.packageName)

            if (checkExistence != 0) {
                img.setImageResource(checkExistence)
            } else {
                img.setImageResource(R.drawable.exam_good)
            }
        }

        /**
         * if its the result mode disables the radioButtons and shows answer
         */
        private fun activateResultMode(questionSample: Question) {
            if (isResult) {
                radio1.setTextColor(colorStateList)
                radio2.setTextColor(colorStateList)
                radio3.setTextColor(colorStateList)
                radio4.setTextColor(colorStateList)

                radio1.isEnabled = false
                radio2.isEnabled = false
                radio3.isEnabled = false
                radio4.isEnabled = false

                when (questionSample.answer) {
                    "1" -> radio1.background =
                        (ContextCompat.getDrawable(itemView.context, R.drawable.border))
                    "2" -> radio2.background =
                        (ContextCompat.getDrawable(itemView.context, R.drawable.border))
                    "3" -> radio3.background =
                        (ContextCompat.getDrawable(itemView.context, R.drawable.border))
                    "4" -> radio4.background =
                        (ContextCompat.getDrawable(itemView.context, R.drawable.border))
                }
            }
        }

        /**
         * binds user last selected answer to question
         */
        private fun setUserLastSelectedAnswer(questionSample: Question) {
            radioGroup.clearCheck()
            when (questionSample.lastanswer) {
                "0" -> radioGroup.clearCheck()
                "1" -> radioGroup.check(R.id.answer1_btn)
                "2" -> radioGroup.check(R.id.answer2_btn)
                "3" -> radioGroup.check(R.id.answer3_btn)
                "4" -> radioGroup.check(R.id.answer4_btn)
            }
        }
    }


    /**
     * removes the other options background if there is any
     * this function is necessary because of binding and recycling
     */
    private fun removeTrueBackgroundFromOtherOptions(
        s: String,
        s1: String,
        s2: String,
        view1: View,
        view2: View,
        view3: View,
        view4: View
    ) {
        if (s == "1" || s1 == "1" || s2 == "1") view1.setBackgroundResource(0)
        if (s == "2" || s1 == "2" || s2 == "2") view2.setBackgroundResource(0)
        if (s == "3" || s1 == "3" || s2 == "3") view3.setBackgroundResource(0)
        if (s == "4" || s1 == "4" || s2 == "4") view4.setBackgroundResource(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        _binding = ItemTestPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewPagerViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val quest = questionList[position]
        holder.bind(quest, position)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}