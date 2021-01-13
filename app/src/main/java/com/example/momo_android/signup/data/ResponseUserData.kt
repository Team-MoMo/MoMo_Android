package com.example.momo_android.signup.data

data class ResponseUserData(
    val message : String,
    val data : SignUp
)

data class SignUp(
    val user : User,
    val token : String
)

data class User(
    val id : Int,
    val email : String,
    val password : String,
    val passwordSalt : String,
    val isAlarmSet : Boolean,
    val alarmTime : String,
    val tempPassword : String,
    val tempPasswordCreatedAt : String,
    val tempPasswordIssueCount : Int,
    val createdAt : String,
    val updatedAt : String
)
