package com.raywenderlich.wewatch.util

import android.app.Activity
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

// Activities/Fragments
fun Activity.display(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.displayError(errorMessage: String) {
    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
}

// Views
fun EditText.text(): String {
    return text.toString()
}

fun ImageView.tag(): String? {
    return tag?.toString()
}
