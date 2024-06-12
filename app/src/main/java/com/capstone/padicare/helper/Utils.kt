package com.capstone.padicare.helper

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun String.toDateFormat(): String? {
    return try {
        val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
        val dateFormatted = inputFormat.parse(this) as Date
        val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", Locale("in", "ID"))
        outputFormat.format(dateFormatted)
    } catch (e: ParseException) {
        e.printStackTrace()
        null
    }
}
