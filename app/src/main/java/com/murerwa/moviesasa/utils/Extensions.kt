package com.murerwa.moviesasa.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.Toast


// Extension functions
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Activity.setTransparentStatusBar() {
    window.statusBarColor = Color.TRANSPARENT
}