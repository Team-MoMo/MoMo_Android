package com.example.momo_android.network

import com.example.momo_android.diary.data.ResponseDiaryData
import com.example.momo_android.home.data.ResponseDiaryList
import com.example.momo_android.list.data.ResponseFilterData
import retrofit2.Call
import retrofit2.http.*

interface RequestInterface {

    // 다이어리 조회
    @Headers("Content-Type: application/json")
    @GET("/diaries/{id}")
    fun getDiary(
        @Header("Authorization") Authorization: String?,
        @Path("id") params: Int
    ) : Call<ResponseDiaryData>

    // 리스트 필터별 조회
    @Headers("Content-Type: application/json")
    @GET("/diaries")
    fun getFilterdDiary(
        @Header("Authorization") Authorization: String?,
        @Query("userId") userId: Int,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("order") order: String,
        @Query("emotionId") emotionId: Int?,
        @Query("depth") depth: Int?
    ) : Call<ResponseFilterData>

    // HomeFragment.kt 다이어리 조회 (일별)
    @Headers("Content-Type: application/json")
    @GET("/diaries")
    fun getHomeDiaryList(
        @Header("Authorization") authorization: String?,
        @Query("order") order: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int,
        @Query("userId") userId: Int
    ): Call<ResponseDiaryList>

    // ScrollFragment.kt 다이어리 조회별 (월별)
    @Headers("Content-Type: application/json")
    @GET("/diaries")
    fun getScrollDiaryList(
        @Header("Authorization") authorization: String?,
        @Query("userId") userId: Int,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("order") order: String
    ): Call<ResponseDiaryList>
}