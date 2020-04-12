package com.me.geoquiz

class Question(textId: Int, answerTrue: Boolean) {
    var id: Int = textId
    var answer: Boolean = answerTrue
    var answered: Boolean = false
}