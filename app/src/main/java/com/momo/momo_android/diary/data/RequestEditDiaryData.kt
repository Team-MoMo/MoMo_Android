package com.momo.momo_android.diary.data

data class RequestEditDiaryData(
    val depth : Int,
    val contents : String,
    val userId : Int,
    val sentenceId : Int,
    val emotionId : Int,
    val wroteAt : String
)
