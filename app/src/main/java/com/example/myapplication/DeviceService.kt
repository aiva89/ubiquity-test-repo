package com.example.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Retrofit Service
interface DeviceService {
    @GET("fingerprint/ui/public.json")
    suspend fun getDevices(): ApiResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://static.ui.com/"
    val service: DeviceService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DeviceService::class.java)
    }
}