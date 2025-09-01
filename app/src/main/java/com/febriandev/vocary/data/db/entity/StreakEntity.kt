package com.febriandev.vocary.data.db.entity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale
import java.util.UUID

@Entity(tableName = "streak")
data class StreakEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val streakType: String, // "APP_OPEN" atau "DAILY_GOAL"
    val date: String,       // format: yyyy-MM-dd (misalnya "2025-08-16")
    val isCompleted: Boolean,
    val isSync: Boolean = false,
)

data class StreakDay(
    val dayName: String,
    val isCompleted: Boolean,
    val week: Int
)

@RequiresApi(Build.VERSION_CODES.O)
fun mapToStreakDays(entities: List<StreakEntity>): List<StreakDay> {
    if (entities.isEmpty()) return emptyList()

    // cari start day (hari pertama streak)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val firstDate = LocalDate.parse(entities.first().date, formatter)

    // bikin list 7 hari dimulai dari firstDate.dayOfWeek
    val startDayOfWeek = firstDate.dayOfWeek
    val days = DayOfWeek.entries

    // rotasi supaya start dari hari streak pertama
    val orderedDays =
        days.dropWhile { it != startDayOfWeek } + days.takeWhile { it != startDayOfWeek }

    return orderedDays.mapIndexed { index, dayOfWeek ->
        val currentDate = firstDate.plusDays(index.toLong())
        val isCompleted =
            entities.any { it.date == currentDate.format(formatter) && it.isCompleted }

        val weekNumber = ((index) / 7) + 1

        StreakDay(
            dayName = dayOfWeek.name.take(3), // Thu, Fri
            isCompleted = isCompleted,
            week = weekNumber
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun mapToStreakDay(entity: StreakEntity?): StreakDay? {
    val date = LocalDate.parse(entity?.date) // format yyyy-MM-dd
    val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())

    return StreakDay(
        dayName = dayName,
        isCompleted = entity?.isCompleted ?: false,
        week = date.get(WeekFields.ISO.weekOfYear())
    )
}
