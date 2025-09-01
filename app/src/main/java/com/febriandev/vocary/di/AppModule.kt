package com.febriandev.vocary.di

import android.app.Application
import androidx.room.Room
import com.febriandev.vocary.data.db.AppDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "vocary.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideGenerateVocabDao(db: AppDatabase) = db.generateVocabDao()

    @Provides
    @Singleton
    fun provideVocabularyDao(db: AppDatabase) = db.vocabularyDao()

    @Provides
    @Singleton
    fun provideGameSessionDao(db: AppDatabase) = db.gameSessionDao()

    @Provides
    @Singleton
    fun provideProgressDao(db: AppDatabase) = db.dailyProgressDao()

    @Provides
    @Singleton
    fun provideStreakDao(db: AppDatabase) = db.streakDao()

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase) = db.userDao()

    @Provides
    @Singleton
    fun provideSearchVocabularyDao(db: AppDatabase) = db.searchVocabularyDao()

    @Provides
    @Singleton
    fun provideFirestore() = FirebaseFirestore.getInstance()
}