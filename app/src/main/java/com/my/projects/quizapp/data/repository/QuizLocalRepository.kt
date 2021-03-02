package com.my.projects.quizapp.data.repository

import androidx.lifecycle.LiveData
import com.my.projects.quizapp.data.local.QuizDB
import com.my.projects.quizapp.data.local.entity.Quiz
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.model.*
import com.my.projects.quizapp.data.remote.QuizResponse
import java.util.*
import javax.inject.Inject


class QuizLocalRepository @Inject constructor(
    private val database: QuizDB
){

   suspend fun saveQuiz(
        quiz: Quiz,
        questions: List<QuestionModel>,
        userAnswers: Map<Int, AnswerModel>
    ) {
        val quizId = database.quizDao.insertQuiz(quiz)
        saveQuizQuestion(quizId, questions, userAnswers)
    }

    private suspend fun saveQuizQuestion(
        quizId: Long,
        questions: List<QuestionModel>,
        userAnswers: Map<Int, AnswerModel>
    ) {
        if (!questions.isNullOrEmpty()) {
            for (i in questions.indices) {
                val questionID =
                    database.quizDao.insertQuestion(questions[i].asQuestionEntity(quizId))
                saveQuestionAnswers(questionID, questions[i].answers, userAnswers[i])
            }
        }
    }

    private suspend fun saveQuestionAnswers(
        questionId: Long,
        answers: List<AnswerModel>,
        userAnswer: AnswerModel?
    ) {
        for (j in answers.indices) {
            if (userAnswer != null && answers[j].id == userAnswer.id)
                database.quizDao.insertAnswer(userAnswer.asAnswerEntity(questionId))
            else
                database.quizDao.insertAnswer(answers[j].asAnswerEntity(questionId))
        }

    }


     suspend fun updateQuiz(
        quiz: Quiz
    ) {
        database.quizDao.updateQuiz(quiz)
    }

   suspend fun deleteQuiz(
        quiz: Quiz
    ) {
        database.quizDao.deleteQuiz(quiz)
    }

    suspend fun findQuizById(quizID: Long): QuizWithQuestionsAndAnswers {
        return database.quizDao.findQuizById(quizID)
    }


    fun findAll(): LiveData<List<QuizWithQuestionsAndAnswers>> {
        return database.quizDao.findAll()
    }

    fun getQuizzesByDate(
        saveDate: Date
    ): LiveData<List<QuizWithQuestionsAndAnswers>> {
        return database.quizDao.getQuizzesByDate(saveDate)
    }

    fun getQuizzesByCategory(
        categoryID: Int
    ): LiveData<List<QuizWithQuestionsAndAnswers>> {
        return database.quizDao.getQuizzesByCategory(categoryID)
    }

    fun getFilteredQuizzes(
        categoryID: Int?,
        saveDate: Date?
    ): LiveData<List<QuizWithQuestionsAndAnswers>> {

        return if (categoryID != null && saveDate == null) {
            database.quizDao.getQuizzesByCategory(categoryID)
        } else if (saveDate != null && categoryID == null) {
            database.quizDao.getQuizzesByDate(saveDate)
        } else if (saveDate != null && categoryID != null) {
            database.quizDao.getQuizzesByDateAndCategory(categoryID, saveDate)
        } else database.quizDao.findAll()

    }

    suspend fun deleteAll() = database.quizDao.deleteAll()




}