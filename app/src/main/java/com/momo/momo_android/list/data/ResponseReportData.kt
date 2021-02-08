package com.momo.momo_android.list.data

data class ResponseReportData(
    val data: Data,
    val message: String
)

data class Data(
    val depthCounts: List<DepthCount>,
    val emotionCounts: List<EmotionCount>
)

data class DepthCount(
    val count: Int,
    val depth: Int
)

data class EmotionCount(
    val count: Int,
    val id: Int
)