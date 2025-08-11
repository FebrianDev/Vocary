package com.febriandev.vocary.di

import android.app.Application
import androidx.room.Room
import com.febriandev.vocary.data.db.AppDatabase
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
        ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideGenerateVocabDao(db: AppDatabase) = db.generateVocabDao()

    @Provides
    @Singleton
    fun provideVocabularyDao(db: AppDatabase) = db.vocabularyDao()



}