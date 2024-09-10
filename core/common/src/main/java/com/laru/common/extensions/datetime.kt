package com.laru.common.extensions

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


/*
    1. 0-24 hours ago --> 11:53
    2. 1-7 days ago   --> Sun
    3. Later          --> 11.11.24
 */

fun Int.toDatetime(): String {
    val dateTime = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(this * 1L),
        ZoneId.systemDefault()
    )
    val now = LocalDateTime.now()

    val durationHours = ChronoUnit.HOURS.between(dateTime, now)
    val durationDays = ChronoUnit.DAYS.between(dateTime, now)

    return when {
        durationDays > 7 -> dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yy"))
        durationHours > 24 -> dateTime.format(DateTimeFormatter.ofPattern("EEE"))
        else -> dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }
}

