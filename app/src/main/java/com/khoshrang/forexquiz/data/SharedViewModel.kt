package com.khoshrang.forexquiz.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khoshrang.forexquiz.R

class SharedViewModel : ViewModel() {

    var questionList: ArrayList<Question> = ArrayList<Question>()

    val currentQuestionNumber: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val premium: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    var testPictures = listOf<Int>(
        R.drawable.test_1,
        R.drawable.test_2,
        R.drawable.test_3,
        R.drawable.test_4,
        R.drawable.test_5,
        R.drawable.test_6,
        R.drawable.test_7,
        R.drawable.test_8,
        R.drawable.test_9,
        R.drawable.test_10,
        R.drawable.test_11,
        R.drawable.test_12,
        R.drawable.test_13
    )

}