package com.example.deamhome.domain.model

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class SignUpInfo(
    val userId: String,
    val pwd: String,
    val confirmPwd: String,
    val sex: Boolean,
    val birthYear: Int,
    val birthMonth: Int,
    val birthDay: Int,
    val zipcode: String,
    val address1: String,
    val email: String,
    val phone: String? = null,
    val receiveMail: Boolean? = null,
    val snsId: String? = null,
    val userName: String,
) {
    // ZoneOffset을 사용하여 LocalDateTime을 ZonedDateTime으로 변환
    private val zonedDateTime: ZonedDateTime =
        LocalDateTime.of(birthYear, birthMonth, birthDay, 0, 0, 0)
            .atZone(ZoneOffset.UTC)

    // DateTimeFormatter를 사용하여 원하는 형식으로 출력
    private val formattedDateTime = zonedDateTime.format(formatter)

    init {
        validateAll()
    }

    private fun validateAll() {
        require(!validateIdFormat(userId)) { "[ERROR] id 유효하지 않습니다. $userId" }
        require(!validatePwdFormat(pwd)) { "[ERROR] pwd 유효하지 않습니다. $pwd" }
        require(!validateIsSamePwd(pwd, confirmPwd)) {
            "[ERROR] pwd 와 confirmPwd 가 다릅니다. $pwd, $confirmPwd"
        }
        require(!validateEmailFormat(email)) { "[ERROR] email 형식이 유효하지 않습니다. $email" }
        require(!validateNameFormat(userName)) { "[ERROR] name 형식이 유효하지 않습니다. $userName" }
        require(!validateBirthDayFormat(birthYear, birthMonth, birthDay)) {
            "[ERROR] 생년월일 정보가 유효하지 않습니다. $birthYear $birthMonth $birthDay"
        }
        phone?.let {
            require(!validatePhoneFormat(it)) {
                "[ERROR] phone 번호가 유효하지 않습니다. $it"
            }
        }
    }

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

        fun validateIdFormat(id: String): Boolean {
            return true
        }

        fun validatePwdFormat(pwd: String): Boolean {
            return true
        }

        fun validateIsSamePwd(pwd: String, confirmPwd: String): Boolean {
            return true
        }

        fun validateEmailFormat(email: String): Boolean {
            return true
        }

        fun validateBirthDayFormat(year: Int, month: Int, day: Int): Boolean {
            runCatching {
                LocalDateTime.of(year, month, day, 0, 0, 0).atZone(ZoneOffset.UTC)
            }.onFailure { return false }
            return true
        }

        fun validatePhoneFormat(phone: String): Boolean {
            return true
        }

        fun validateNameFormat(name: String): Boolean {
            return true
        }
    }
}
