package com.my.projects.quizapp.data.model

import com.my.projects.quizapp.data.local.entity.Answer
import com.my.projects.quizapp.data.local.entity.Question
import java.io.Serializable

data class QuizModel(
    val questions: List<QuestionModel>
)

data class QuestionModel(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val answers: List<AnswerModel>
)

data class AnswerModel(
    val id: Int,
    val answer: String,
    val isCorrect: Boolean,
    val isUser: Boolean = false
)

data class CategoryModel(
    val id: Int,
    val name: String,
    val icon: Int
) : Serializable

data class QuizSetting(
    val amount: Int,
    val category: Int? = -1,
    val type: String? = "",
    val difficulty: String? = "",
) : Serializable


fun QuestionModel.asQuestionEntity(quizID: Long): Question {
    return Question(quizID, category, type, difficulty, question)
}

fun AnswerModel.asAnswerEntity(questionID: Long): Answer {
    return Answer(questionID, answer, isCorrect, isUser)
}