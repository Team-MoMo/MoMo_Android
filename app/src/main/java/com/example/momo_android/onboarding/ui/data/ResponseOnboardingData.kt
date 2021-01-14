package com.example.momo_android.onboarding.ui

data class ResponseOnboardingData(
    val message:String,
    val data: Onboarding
)
data class Onboarding(
    val emotionId:Int,
    val sentence_01:sentence01,
    val sentence_02:sentence02,
    val sentence_03:sentence03
)
data class sentence01(
    val contents:String,
    val bookName:String,
    val writer:String,
    val publisher:String
)

data class sentence02(
    val contents:String,
    val bookName:String,
    val writer:String,
    val publisher:String
)

data class sentence03(
    val contents:String,
    val bookName:String,
    val writer:String,
    val publisher:String
)