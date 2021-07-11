package com.example.todo.data.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    private const val BASE_URL = "https://d5dps3h13rv6902lp5c8.apigw.yandexcloud.net"
    private const val TOKEN = "016bb14901fc42d0bfa2d6688ae84c42"

    private val client = OkHttpClient.Builder().addInterceptor { chain ->
        val original = chain.request()

        val builder = original.newBuilder()
            .header("Authorization: Bearer ", TOKEN)

        val request = builder.build()
        chain.proceed(request)
    }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()


    private fun getRetrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}