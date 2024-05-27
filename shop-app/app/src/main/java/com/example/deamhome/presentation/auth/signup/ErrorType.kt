package com.example.deamhome.presentation.auth.signup

enum class ErrorType(val tag: String) {
    ID("id_error"),
    PWD("pwd_error"),
    NAME("name_error"),
    PHONE("phone_error"),
    EMAIL("email_error"),
    ANY("error"),
}