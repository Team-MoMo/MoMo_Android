package com.example.momo_android.login.data

data class ResponseTempPasswordData(
    val message : String,
    val data : TempPassword
)

data class TempPassword(
    val email : String,
    val tempPasswordIssueCount : Int
)
