package com.abov.moviehub.presentation.util

import android.content.Context
import com.abov.moviehub.R
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toUserMessage(context: Context): String = when (this) {
    is UnknownHostException,
    is ConnectException -> context.getString(R.string.error_no_internet)
    is SocketTimeoutException -> context.getString(R.string.error_timeout)
    is HttpException -> context.getString(R.string.error_server_format, code())
    else -> context.getString(R.string.error_generic_message)
}

