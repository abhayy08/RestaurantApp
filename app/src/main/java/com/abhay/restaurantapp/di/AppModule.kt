package com.abhay.restaurantapp.di

import com.abhay.restaurantapp.data.api.FoodServiceApi
import com.abhay.restaurantapp.data.repository.FoodRepositoryImpl
import com.abhay.restaurantapp.domain.FoodRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://uat.onebanc.ai")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideFoodServiceApi(retrofit: Retrofit): FoodServiceApi {
        return retrofit.create(FoodServiceApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFoodRepository(foodServiceApi: FoodServiceApi): FoodRepository {
        return FoodRepositoryImpl(foodServiceApi)
    }
}