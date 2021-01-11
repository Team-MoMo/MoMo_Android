package com.example.momo_android.network

import com.example.momo_android.diary.data.ResponseDiaryData
import com.example.momo_android.home.data.ResponseHomeDiary
import retrofit2.Call
import retrofit2.http.*

interface RequestInterface {

    // 다이어리 조회
    @Headers("Content-Type: application/json")
    @GET("/diaries/{id}")
    fun getDiary(
        @Header("Authorization") Authorization: String?,
        @Path("id") params: Int
    ): Call<ResponseDiaryData>

    // HomeFragment.kt 다이어리 조회
    @Headers("Content-Type: application/json")
    @GET("/diaries")
    fun getHomeDiary(
        @Header("Authorization") authorization: String?,
        @Query("order") order: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int,
        @Query("userId") userId: Int
    ): Call<ResponseHomeDiary>
}