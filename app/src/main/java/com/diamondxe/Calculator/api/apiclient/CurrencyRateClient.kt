package com.dxe.calc.api.apiclient

import com.diamondxe.Calculator.BuildConfig
import com.dxe.calc.api.apiinterface.CurrencyRateApiInterface
import com.dxe.calc.api.common.NetworkResponseAdapterFactory
import com.dxe.calc.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object CurrencyRateClient {
    private val builder = Retrofit.Builder()
        .baseUrl(Constants.URL_CURRENCY_RATE)
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())

    private var retrofit = builder.build()

    private val httpClient = OkHttpClient.Builder()

    @Volatile
    private var instance : CurrencyRateApiInterface? = null
    fun getInstance() : CurrencyRateApiInterface = instance ?: synchronized(this) {
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        httpClient.interceptors().clear()
        if (BuildConfig.DEBUG) {
            httpClient.addInterceptor(getLoggingInterceptor())
        }
        /*httpClient.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", "Basic XtGO0XJR8qdh55TjodAXCE7835Yj7HuFGc2ft")
                .build()
            chain.proceed(request)
        }*/
        builder.client(httpClient.build())
        retrofit = builder.build()
        retrofit.create(CurrencyRateApiInterface::class.java)
    }




    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
//        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        return logging
    }

}