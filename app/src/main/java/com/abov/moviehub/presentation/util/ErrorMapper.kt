package com.abov.moviehub.presentation.util

import android.content.Context
import com.abov.moviehub.R
import retrofit2.HttpException
import java.net.UnknownHostException

fun Throwable.toUserMessage(context: Context): String = when (this) {
    is UnknownHostException -> context.getString(R.string.error_no_internet)
    is HttpException -> context.getString(R.string.error_server_format, code())
    else -> context.getString(R.string.error_generic_message)
}

