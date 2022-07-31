package com.joshuakarl.serinotest.util

import android.icu.text.NumberFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object NumberFormatter {
    fun getCurrencyString(n: Float): String {
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.maximumFractionDigits = 2
        return currencyFormat.format(n)
    }

    fun getRatingString(n: Float): String {
        val ratingFormat = NumberFormat.getInstance()
        ratingFormat.maximumFractionDigits = 2
        return ratingFormat.format(n)
    }

    fun getDateString(timestamp: Long): String {
        val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
        val dateFormat = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        return dateFormat.format(date)
    }
}