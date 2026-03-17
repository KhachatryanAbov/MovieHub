package com.abov.moviehub.presentation.util

import com.abov.moviehub.R
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toUserMessage(): UiText = when (this) {
    is UnknownHostException,
    is ConnectException -> UiText(R.string.error_no_internet)
    is SocketTimeoutException -> UiText(R.string.error_timeout)
    is HttpException -> UiText(R.string.error_server_format, args = listOf(code()))
    else -> UiText(R.string.error_generic_message)
}

