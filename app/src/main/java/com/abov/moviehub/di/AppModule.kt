package com.abov.moviehub.di

import com.abov.moviehub.data.remote.ApiService
import com.abov.moviehub.data.repository.MovieRepositoryImpl
import com.abov.moviehub.domain.repository.MovieRepository
import com.abov.moviehub.domain.usecase.GetMovieDetailUseCase
import com.abov.moviehub.domain.usecase.GetPagedMoviesUseCase
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

    private const val BASE_URL = "https://api.tvmaze.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideMovieRepository(apiService: ApiService): MovieRepository =
        MovieRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun provideGetPagedMoviesUseCase(repository: MovieRepository): GetPagedMoviesUseCase =
        GetPagedMoviesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetMovieDetailUseCase(repository: MovieRepository): GetMovieDetailUseCase =
        GetMovieDetailUseCase(repository)
}
