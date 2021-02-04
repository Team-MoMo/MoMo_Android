package com.momo.momo_android.upload.data

data class ResponseSentenceData(
    val data: List<Data>,
    val message: String
)

data class Data(
    val bookName: String,
    val contents: String,
    val createdAt: String,
    val id: Int,
    val publisher: String,
    val updatedAt: String,
    val writer: String
)