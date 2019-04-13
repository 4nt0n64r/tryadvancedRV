package com.a4nt0n64r.tryadvancedrv

import android.graphics.drawable.Drawable

object DrawableUtils {
    private val EMPTY_STATE = intArrayOf()

    fun clearState(drawable: Drawable?) {
        if (drawable != null) {
            drawable.state = EMPTY_STATE
        }
    }
}