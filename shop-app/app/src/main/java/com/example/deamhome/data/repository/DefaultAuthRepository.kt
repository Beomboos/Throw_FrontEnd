package com.example.deamhome.data.repository

import android.util.Log
import com.example.deamhome.data.datasource.local.LocalAuthDataSource
import com.example.deamhome.data.datasource.network.NetworkAuthDataSource
import com.example.deamhome.data.model.request.LoginRequest
import com.example.deamhome.data.model.response.Token
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class DefaultAuthRepository(
    private val localAuthDataSource: LocalAuthDataSource,
    private val networkAuthDataSource: NetworkAuthDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AuthRepository {
    override val isLogin: Boolean
        get() = localAuthDataSource.isLogin();
    override suspend fun login(id: String, pwd: String): ApiResponse<Token> {
        return withContext(dispatcher) {
            val response = networkAuthDataSource.login(
                LoginRequest(
                    inputId = id,
                    inputPassword = pwd,
                ),
            )


            Log.d("HTTP_LOG", response.toString())
            if (response is ApiResponse.Success) {
                Log.d("HTTP_LOG","Success")
                response.body?.let {
                    localAuthDataSource.updateToken(response.body)
                }
            }

            response
        }
    }

    // 토큰을 내부 저장소에서 가져오는
    override fun getToken(): Token {
        return localAuthDataSource.getToken()
    }

    // 토큰을 갱신하는데 사용
    override fun refreshToken() {
        runBlocking {
            val response =
                networkAuthDataSource.refreshToken() // 여기도 나중에 ApiResponse가 Success인 경우만 update하도록 수정해야함.
            if (response is ApiResponse.Success) {
                localAuthDataSource.updateToken(response.body)
            }
        }
    }

    override fun removeToken() {
        runBlocking {
            localAuthDataSource.removeToken()
        }
    }

    override fun updateToken(token: Token) {
        runBlocking {
            localAuthDataSource.updateToken(token)
        }
    }

//    override fun signUp(signUpRequest: SignUpRequest): ApiResponse<String> {
//        return withContext(dispatcher) {
//            networkAuthDataSource.signUp(signUpRequest)
//        }
//    }
}
