package com.febriandev.vocary.data.db.converters

import androidx.room.TypeConverter
import com.febriandev.vocary.data.db.entity.PhoneticEntity
import com.febriandev.vocary.data.db.entity.UnifiedDefinitionEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object VocabularyConverters {
    private val gson = Gson()

    // PhoneticEntity
    @TypeConverter
    fun fromPhoneticList(value: List<PhoneticEntity>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toPhoneticList(value: String): List<PhoneticEntity> {
        val type = object : TypeToken<List<PhoneticEntity>>() {}.type
        return gson.fromJson(value, type)
    }

    // UnifiedDefinitionEntity
    @TypeConverter
    fun fromDefinitionList(value: List<UnifiedDefinitionEntity>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toDefinitionList(value: String): List<UnifiedDefinitionEntity> {
        val type = object : TypeToken<List<UnifiedDefinitionEntity>>() {}.type
        return gson.fromJson(value, type)
    }

    // --- List<String> (sourceUrls) ---
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }
}