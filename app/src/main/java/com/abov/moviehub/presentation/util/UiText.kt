package com.abov.moviehub.presentation.util

import android.content.Context
import androidx.annotation.StringRes

class UiText(
    @field:StringRes val resId: Int,
    val args: List<Any> = emptyList()
) {
    fun asString(context: Context): String {
        return if (args.isEmpty()) {
            context.getString(resId)
        } else {
            context.getString(resId, *args.toTypedArray())
        }
    }
}
