package com.abov.moviehub.presentation.util

import androidx.annotation.StringRes
import com.abov.moviehub.R
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@StringRes
fun Throwable.toUserMessage(): Int = when (this) {
    is UnknownHostException,
    is ConnectException -> R.string.error_no_internet
    is SocketTimeoutException -> R.string.error_timeout
    is HttpException -> R.string.error_generic_message
    else -> R.string.error_generic_message
}

