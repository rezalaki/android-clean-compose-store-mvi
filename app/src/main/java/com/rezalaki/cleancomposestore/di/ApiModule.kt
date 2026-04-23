package com.rezalaki.cleancomposestore.di

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rezalaki.cleancomposestore.data.remote.ApiService
import com.rezalaki.cleancomposestore.utils.Constants.FAKE_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.logicbase.mockfit.MockFitConfig
import ir.logicbase.mockfit.MockFitInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {


    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .create()


    /**
     * `MockFitInterceptor`
     *
     * Github link: [https://github.com/javaherisaber/MockFit](https://github.com/javaherisaber/MockFit)
     *
     * This interceptor is used to mock ApiService responses with json files.
     *
     * Json files are located in `fakeData` directory and `fakeData` is located in
     * `assets` directory: [assets/fakeData](app/src/main/assets/fakeData)
     *
     * =================================
     *
     * To see api calls, you can filter `Logcat` by `API-CALLS` tag.
     *
     *
     */
    @Provides
    @Singleton
    fun provideMockFitInterceptor(@ApplicationContext context: Context): MockFitInterceptor =
        MockFitInterceptor(
            bodyFactory = { context.resources.assets.open(it) },
            logger = { _, msg -> Log.d("API-CALLS", msg) },
            baseUrl = FAKE_BASE_URL,
            requestPathToJsonMap = MockFitConfig.REQUEST_TO_JSON,
            mockFilesPath = "fakeData",
            mockFitEnable = true,
            apiEnableMock = true,
            apiIncludeIntoMock = emptyArray(),
            apiExcludeFromMock = emptyArray(),
            apiResponseLatency = 1_500L
        )


    @Provides
    @Singleton
    fun provideClient(mockFitInterceptor: MockFitInterceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)

        httpClient.addInterceptor(mockFitInterceptor)

        return httpClient.build()
    }


    @Provides
    @Singleton
    fun provideApiService(gson: Gson, client: OkHttpClient): ApiService =
        Retrofit.Builder()
            .baseUrl(FAKE_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)

}