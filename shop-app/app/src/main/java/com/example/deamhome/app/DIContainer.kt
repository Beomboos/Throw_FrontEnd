package com.example.deamhome.app

import android.app.Application
import android.content.Context
import android.provider.Settings.Global.getString
import androidx.datastore.dataStore
import com.example.deamhome.R
import com.example.deamhome.data.datasource.local.LocalAuthDataSource
import com.example.deamhome.data.datasource.network.NetworkAuthDataSource
import com.example.deamhome.data.datasource.network.NetworkKakaoDataSource
import com.example.deamhome.data.datasource.network.NetworkMailDataSource
import com.example.deamhome.data.datasource.network.NetworkProductDataSource
import com.example.deamhome.data.datasource.network.NetworkStoreDataSource
import com.example.deamhome.data.model.response.TokenSerializer
import com.example.deamhome.data.repository.DefaultAuthRepository
import com.example.deamhome.data.repository.DefaultKakaoRepository
import com.example.deamhome.data.repository.DefaultMailReposity
import com.example.deamhome.data.repository.DefaultProductRepository
import com.example.deamhome.data.repository.DefaultStoreRepository
import com.example.deamhome.data.retrofit.AuthRetrofit
import com.example.deamhome.data.retrofit.AuthService
import com.example.deamhome.data.retrofit.DefaultRetrofit
import com.example.deamhome.data.retrofit.KakaoRetrofit
import com.example.deamhome.data.retrofit.KakaoService
import com.example.deamhome.data.retrofit.MailRetrofit
import com.example.deamhome.data.retrofit.MailService
import com.example.deamhome.data.retrofit.ProductService
import com.example.deamhome.data.retrofit.StoreService
import com.example.deamhome.data.secure.CryptoManager
import com.example.deamhome.domain.repository.AuthRepository
import com.example.deamhome.domain.repository.KakaoRepository
import com.example.deamhome.domain.repository.MailRepository
import com.example.deamhome.domain.repository.ProductRepository
import com.example.deamhome.domain.repository.StoreRepository

// 객체 생성을 애플리케이션 객체 초기화시 이렇게 모두 해버리면 앱 시작이 늦어질 수 있음. 싱글톤 패턴으로 분리예정
class DIContainer(
    application: Application,
    deviceId: String,
) {
//    private val Context.dataStore by dataStore(
//        fileName = LocalAuthDataSource.AUTH_TOKEN_STORE_NAME,
//        serializer = TokenSerializer(CryptoManager()),
//    )

    val sharedPref = application.getSharedPreferences(
        LocalAuthDataSource.AUTH_TOKEN_STORE_NAME, Context.MODE_PRIVATE)

    private val localAuthDataSource: LocalAuthDataSource =
        LocalAuthDataSource(sharedPref)

    // 로깅용 인터셉터로 공용임.
//    private val loggingInterceptor = LoggingApolloInterceptor(deviceId)

    // 토큰 삽입을 하지 않고 요청을 보내는 아폴로 클라이언트
//    private val authApolloClient: ApolloClient =
//        ApolloClient.Builder().serverUrl(BuildConfig.SERVER_URL)
//            .addInterceptor(loggingInterceptor)
//            .build()

    private val authClient = AuthRetrofit.createInstance()
    private val authService = authClient.create(AuthService::class.java)
    private val networkAuthDataSource = NetworkAuthDataSource(authService)
    val authRepository: AuthRepository =
        DefaultAuthRepository(localAuthDataSource, networkAuthDataSource)

    val isLogin = authRepository.isLogin

    // 토큰 자동 삽입 및 갱신 기능이 있는 유저 관련 네트워크 클라이언트 만듬
    private val productClient = DefaultRetrofit.createInstance(authRepository)
    private val productService = productClient.create(ProductService::class.java)
    private val networkProductDataSource = NetworkProductDataSource(productService)
    val productRepository: ProductRepository = DefaultProductRepository(networkProductDataSource)

    // 토큰 자동 삽입 및 갱신 기능이 있는 가게 관련 네트워크 클라이언트 만듬
    private val storeClient = DefaultRetrofit.createInstance(authRepository)
    private val storeService = storeClient.create(StoreService::class.java)
    private val networkStoreDataSource = NetworkStoreDataSource(storeService)
    val storeRepository: StoreRepository = DefaultStoreRepository(networkStoreDataSource)

    private val kakaoClient = KakaoRetrofit.createInstance()
    private val kakaoService = kakaoClient.create(KakaoService::class.java)
    private val networkKakaoDataSource = NetworkKakaoDataSource(kakaoService)
    val kakaoRepository: KakaoRepository = DefaultKakaoRepository(networkKakaoDataSource)

    private val mailClient = MailRetrofit.createInstance()
    private val mailService = mailClient.create(MailService::class.java)
    private val networkMailDataSource = NetworkMailDataSource(mailService)
    val mailRepository: MailRepository = DefaultMailReposity(networkMailDataSource)
}