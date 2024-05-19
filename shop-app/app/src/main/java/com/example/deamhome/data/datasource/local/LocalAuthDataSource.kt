package com.example.deamhome.data.datasource.local

import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import com.example.deamhome.data.model.response.Token
import com.example.deamhome.data.model.response.Token.Companion.EMPTY
import com.example.deamhome.data.model.response.Token.Companion.EMPTY_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class LocalAuthDataSource(private val sharedPref: SharedPreferences) {
//    val isLogin: Boolean = dataStore.data
//        .catch { exception ->
//            // dataStore.data throws an IOException when an error is encountered when reading data
//            if (exception is IOException) {
//                emit(EMPTY_TOKEN)
//            } else {
//                throw exception
//            }
//        }.map {
//            it.accessToken != EMPTY
//        }
    fun getToken(): Token{
        return Token(
            sharedPref.getString("accessToken", "") ?:"",
            sharedPref.getString("refreshToken", "") ?:""
        )
    }

    fun isLogin(): Boolean{
        if(getToken().accessToken != ""){
            return true;
        }
        return false;
    }

    fun updateToken(newToken: Token) {
        with (sharedPref.edit()) {
            putString("accessToken", newToken.accessToken);
            putString("refreshToken", newToken.refreshToken);
            apply()
        }
    }

    fun removeToken() {
        with (sharedPref.edit()) {
            remove("accessToken");
            remove("refreshToken");
            apply()
        }
    }

    companion object {
        const val AUTH_TOKEN_STORE_NAME = "auth_token_data_store.json"
    }
}
