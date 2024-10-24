package com.laru.common.extensions

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


/*
    1. > 7 days     --> 11.11.24
    2. > 24 hours   --> Sunday
    3. > 1 hour     --> 11:53
    4. > 15 minutes --> 25 minutes ago
    5. < 15 minutes --> recently
 */
fun Int.toDatetime(): String {
    val dateTime = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(this * 1L),
        ZoneId.systemDefault()
    )
    val now = LocalDateTime.now()

    val durationMinutes = ChronoUnit.MINUTES.between(dateTime, now)
    val durationHours = ChronoUnit.HOURS.between(dateTime, now)
    val durationDays = ChronoUnit.DAYS.between(dateTime, now)

    return when {
        durationDays > 7 -> dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yy"))
        durationHours > 24 -> dateTime.format(DateTimeFormatter.ofPattern("EEEE"))
        durationHours > 1 -> dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        else -> durationMinutes.toString() // < 1 hour
    }
}


sealed class TimeElapsed {
    data class OnDate(val date: String) : TimeElapsed()
    data class OnDay(val day: String) : TimeElapsed()
    data class AtTime(val time: String) : TimeElapsed()
    data class MinutesAgo(val minutes: String) : TimeElapsed()
    data object Recently : TimeElapsed()
}

fun Int.toTimeElapsed(): TimeElapsed {
    val dateTime = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(this * 1L),
        ZoneId.systemDefault()
    )
    val now = LocalDateTime.now()

    val durationMinutes = ChronoUnit.MINUTES.between(dateTime, now)
    val durationHours = ChronoUnit.HOURS.between(dateTime, now)
    val durationDays = ChronoUnit.DAYS.between(dateTime, now)

    return when {
        durationDays > 7 -> TimeElapsed.OnDate(dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yy")))
        durationHours > 24 -> TimeElapsed.OnDay(dateTime.format(DateTimeFormatter.ofPattern("EEEE")))
        durationHours > 0 -> TimeElapsed.AtTime(dateTime.format(DateTimeFormatter.ofPattern("HH:mm")))
        durationMinutes > 14 -> TimeElapsed.MinutesAgo(durationMinutes.toString())
        else -> TimeElapsed.Recently
    }
}

fun String.getDaysUntilBirthday(): Int {
    val today = LocalDate.now()
    val currentYear = LocalDate.now().year
    val formatter = DateTimeFormatter.ofPattern("d.M.yyyy")
    val birthday = if (this.length < 6) "$this.$currentYear" else this.replaceAfterLast('.', currentYear.toString())

    val date = LocalDate.parse(birthday, formatter)
    val birthdayDate = if (date.isBefore(today)) date.withYear(currentYear + 1) else date

    return ChronoUnit.DAYS.between(today, birthdayDate).toInt()
}
