package com.example.momo_android.upload.data

data class ResponseUploadDiaryData(
    val data: DiaryData,
    val message: String
)

data class DiaryData(
    val id: Int,
    val contents: String,
    val depth: Int,
    val userId: Int,
    val sentenceId: Int,
    val emotionId: Int,
    val wroteAt: String,
    val position: Int,
    val createdAt: String,
    val updatedAt: String
)