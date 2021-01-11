package com.example.momo_android.list.data

data class ResponseFilterData(
    val data: List<ListData>,
    val message: String
)

data class ListData(
    val Emotion: ListEmotion,
    val Sentence: ListSentence,
    val contents: String,
    val createdAt: String,
    val depth: Int,
    val emotionId: Int,
    val id: Int,
    val position: Int,
    val sentenceId: Int,
    val updatedAt: String,
    val userId: Int,
    val wroteAt: String
)

data class ListEmotion(
    val createdAt: String,
    val id: Int,
    val name: String,
    val updatedAt: String
)

data class ListSentence(
    val bookName: String,
    val contents: String,
    val createdAt: String,
    val id: Int,
    val publisher: String,
    val updatedAt: String,
    val writer: String
)
