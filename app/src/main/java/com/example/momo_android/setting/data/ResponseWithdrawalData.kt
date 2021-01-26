package com.example.momo_android.setting

data class ResponseWithdrawalData(
    val message:String,
    val data: Data
)

data class Data(
    val id:Int,
    val email:String,
    val password:String,
    val passwordSalt:String,
    val isAlarmset: Boolean,
    val alarmTime: String,
    val tempPassword:String,
    val tempPasswordCreatedAt:String,
    val tempPasswordIssueCount:Int,
    val createdAt: String,
    val updatedAt:String
)