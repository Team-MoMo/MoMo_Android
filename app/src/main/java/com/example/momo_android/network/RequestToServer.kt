package com.example.momo_android.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RequestToServer {

    private var retrofit = Retrofit.Builder()
        .baseUrl("http://3.36.79.14:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //실체 구현체 생성 부분
    var service: RequestInterface = retrofit.create<RequestInterface>(RequestInterface::class.java)

}