package com.murerwa.moviesasa.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.murerwa.moviesasa.models.Movie


// Extension functions
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Fragment.hideSoftKeyboard() {
    val view = requireActivity().currentFocus

    view?.let {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


}

fun Fragment.makeSnackBar(): Snackbar {
    val parentLayout = requireActivity().findViewById<View>(android.R.id.content)
    return Snackbar.make(
        parentLayout,
        "Posting review...",
        Snackbar.LENGTH_INDEFINITE
    )
}

private val clickTag = "__click__"
fun View.blockingClickListener(debounceTime: Long = 1200L,action: (Movie) -> Unit, movie: Movie) {
    Log.d(clickTag, "Method called")
    var lastClickTime: Long? = null

    this.setOnClickListener(object : View.OnClickListener {
        override fun onClick(v: View) {
            Log.d(clickTag, "On click called")
            if (lastClickTime == null) {
                Log.d(clickTag, "Click happened")
                lastClickTime = SystemClock.elapsedRealtime()
                action(movie)
            } else {
                Log.d(clickTag, "Click did not happen")
                val timeNow = SystemClock.elapsedRealtime()
                val elapsedTimeSinceLastClick = timeNow - lastClickTime!!
                Log.d(clickTag, """
                        DebounceTime: $debounceTime
                        Time Elapsed: $elapsedTimeSinceLastClick
                        Is within debounce time: ${elapsedTimeSinceLastClick < debounceTime}
                    """.trimIndent())

                if (elapsedTimeSinceLastClick < debounceTime) {
                    Log.d(clickTag, "Double click shielded")
                    return
                }
                else {
                    Log.d(clickTag, "Click happened")
                    action(movie)
                }
                lastClickTime = SystemClock.elapsedRealtime()
            }
        }
    })
}