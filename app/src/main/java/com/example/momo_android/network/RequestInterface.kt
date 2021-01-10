package com.example.momo_android.network

import com.example.momo_android.diary.data.ResponseDiaryData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface RequestInterface {

    // 다이어리 조회
    @Headers("Content-Type: application/json")
    @GET("/diaries/{id}")
    fun getDiary() : Call<ResponseDiaryData>


}