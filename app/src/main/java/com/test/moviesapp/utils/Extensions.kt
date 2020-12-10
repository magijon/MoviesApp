package com.test.moviesapp.utils

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build

fun String.addEllipsis() : String =
    if (this.isNotBlank()) {
        var dots = ""
        for (i in 0..2) {
            if (this.substring(this.length - (i + 1), this.length - i) != ".")
                dots = "$dots."
        }
        "$this$dots"
    }
    else
        this


fun Drawable.setColorFilter(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
    } else {
        setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
}


