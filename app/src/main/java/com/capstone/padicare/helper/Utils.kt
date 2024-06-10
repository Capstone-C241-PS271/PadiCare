package com.capstone.padicare.helper

import android.content.Context
import android.net.Uri
import java.io.File
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun uriToFile(imageUri: Uri, context: Context) : File {
    return File(context.cacheDir, "${System.currentTimeMillis()}_temp.jpg")
}

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
