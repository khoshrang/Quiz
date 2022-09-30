package com.khoshrang.forexquiz.data

data class Question(
    var question: String,
    var answer: String,
    var testId: String,
    var option1: String,
    var option2: String,
    var option3: String,
    var option4: String,
    var lastanswer: String
)
