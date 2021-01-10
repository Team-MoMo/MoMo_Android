package com.example.momo_android.diary.data

data class ResponseDiaryData (
    val message : String,
    val data : Diary
)

data class Diary(
    val id : Int,
    val content : String,
    val position : Int,
    val depth : Int,
    val userId : Int,
    val sentenceId : Int,
    val emotionId : Int,
    val wroteAt : String,
    val createdAt : String,
    val updatedAt : String,
    val Sentence : Sentence,
    val Emotion : Emotion
)

data class Sentence(
    val id : Int,
    val content : String,
    val bookName : String,
    val writer : String,
    val publisher : String,
    val emotionId : Int,
    val createdAt : String,
    val updatedAt : String
)

data class Emotion(
    val id : Int,
    val name : String,
    val createdAt : String,
    val updatedAt : String
)
