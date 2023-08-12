package com.dbvertex.job.network.retrofit

import com.dbvertex.job.JobApp
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private fun getCache(): Cache {
    val cacheSize = (10 * 1024 * 1024).toLong()
    val cacheDir = JobApp.instance.cacheDir
    return Cache(cacheDir, cacheSize)
}

private fun getOkHttpClient(): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    //for performing request in parallel
    val dispatcher = Dispatcher().apply {
        maxRequests = 3
    }

    return OkHttpClient.Builder()
        .cache(getCache())
        .dispatcher(dispatcher)
        .addInterceptor(loggingInterceptor)
        .addNetworkInterceptor(OnlineCachingInterceptor())
        .addInterceptor(OfflineCachingInterceptor())


        .build()
}

private fun getRetrofitBuilder(): Retrofit.Builder {
    return Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/")
        .client(getOkHttpClient())

        .addConverterFactory(GsonConverterFactory.create())
}

fun <T> getAutoRetrofitService(clazz: Class<T>): T {
    return getRetrofitBuilder().build().create(clazz)
}
