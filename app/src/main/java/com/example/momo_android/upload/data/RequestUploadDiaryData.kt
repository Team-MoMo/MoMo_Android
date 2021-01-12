package com.example.momo_android.upload.data

data class RequestUploadDiaryData(
    val contents: String,
    val depth: Int,
    val userId: Int,
    val sentenceId: Int,
    val emotionId: Int,
    val wroteAt: String
)