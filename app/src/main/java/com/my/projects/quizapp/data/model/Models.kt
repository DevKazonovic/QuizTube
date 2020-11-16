package com.my.projects.quizapp.data.model

import com.my.projects.quizapp.data.db.entity.Answer
import com.my.projects.quizapp.data.db.entity.Question
import java.io.Serializable

data class QuizModel(
    val questions: List<QuestionModel>
)

data class QuestionModel(
    val category: String,
    val type: String,
    val difficulty: String,
    val question:String,
    val answers: List<AnswerModel>
)

data class AnswerModel(
    val answer: String,
    val isCorrect: Boolean,
    val isUser:Boolean=false
)

data class QuizSetting(
    val amount: Int,
    val category: Int?=null,
    val type: String?="",
    val difficulty: String?="",
): Serializable


fun QuestionModel.asQuestionEntity(quizID:Long):Question{
    return Question(quizID,category,type,difficulty,question)
}

fun AnswerModel.asAnswerEntity(questionID:Long,isUser:Boolean):Answer{
    return Answer(questionID,answer,isCorrect,isUser)
}