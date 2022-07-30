package com.joshuakarl.serinotest.util

import android.icu.text.NumberFormat

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
}