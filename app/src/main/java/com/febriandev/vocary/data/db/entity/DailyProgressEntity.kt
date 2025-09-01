package com.febriandev.vocary.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "daily_progress")
@TypeConverters(ListVocabularyConverters::class)
data class DailyProgressEntity(
    @PrimaryKey val date: String, // format yyyy-MM-dd
    val progress: Int,
    val isGoalAchieved: Boolean = false,
    val listVocabulary: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isSync: Boolean = false,
)

class ListVocabularyConverters {
    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toList(data: String): List<String> {
        return if (data.isEmpty()) emptyList() else data.split(",")
    }
}
