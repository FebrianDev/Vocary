package com.febriandev.vocary.di

import com.febriandev.vocary.BuildConfig
import com.febriandev.vocary.data.api.DictionaryApiService
import com.febriandev.vocary.data.api.OpenAiApiService
import com.febriandev.vocary.data.api.WordsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

// OpenAiQualifier.kt
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OpenAi

// DictionaryQualifier.kt
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Dictionary

//WordsQualifier
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Words

//@Qualifier
//@Retention(AnnotationRetention.BINARY)
//annotation class Translate

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @OpenAi
    fun provideOpenAiRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL_OPEN_AI)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @Dictionary
    fun provideDictionaryRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL_DICTIONARY)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @Words
    fun provideWordsRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL_WORDS_API)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

//    @Provides
//    @Singleton
//    @Translate
//    fun provideTranslateApiRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
//        .baseUrl(BuildConfig.BASE_URL_TRANSLATE)
//        .client(okHttpClient)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()

    @Provides
    @Singleton
    fun provideOpenAiService(@OpenAi retrofit: Retrofit): OpenAiApiService =
        retrofit.create(OpenAiApiService::class.java)

    @Provides
    @Singleton
    fun provideDictionaryService(@Dictionary retrofit: Retrofit): DictionaryApiService =
        retrofit.create(DictionaryApiService::class.java)

    @Provides
    @Singleton
    fun provideWordsService(@Words retrofit: Retrofit): WordsApiService =
        retrofit.create(WordsApiService::class.java)


//    @Provides
//    @Singleton
//    fun provideAzureService(@Translate retrofit: Retrofit): AzureTranslateService =
//        retrofit.create(AzureTranslateService::class.java)

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

}
