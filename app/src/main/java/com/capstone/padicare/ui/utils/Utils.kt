package com.capstone.padicare.ui.utils

import android.content.Context
import android.net.Uri
import java.io.File

fun uriToFile(imageUri: Uri, context: Context) : File {
    return File(context.cacheDir, "${System.currentTimeMillis()}_temp.jpg")
}