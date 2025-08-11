package com.febriandev.vocary.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONArray

@Entity(tableName = "generate_vocab")
data class GeneratedVocabEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val word: String = "",
    val isStatus: Boolean = false,
)

fun convertToVocabListFromArray(json: String): List<GeneratedVocabEntity> {
    return try {
        val jsonArray = JSONArray(json)
        (0 until jsonArray.length()).map { index ->
            val word = jsonArray.getString(index)
            GeneratedVocabEntity(
                word = word,
                isStatus = false
            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}