package com.example.momo_android.home.data

import com.google.gson.annotations.SerializedName

data class ResponseDiaryList(
    val message: String,
    val data: List<Data>
) {
    data class Data(
        val id: Int,
        val contents: String,
        val depth: Int,
        val userId: Int,
        val sentenceId: Int,
        val emotionId: Int,
        val wroteAt: String,
        val position: Int,
        val createdAt: String,
        val updatedAt: String,
        @SerializedName("Sentence")
        val sentence: Sentence,
        @SerializedName("Emotion")
        val emotion: Emotion
    ) {
        data class Sentence(
            val id: Int,
            val contents: String,
            val bookName: String,
            val writer: String,
            val publisher: String,
            val createdAt: String,
            val updatedAt: String
        )

        data class Emotion(
            val id: Int,
            val name: String,
            val createdAt: String,
            val updatedAt: String
        )
    }
}