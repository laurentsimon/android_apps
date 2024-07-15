package com.example.myapplication

import android.util.Log
import androidx.activity.ComponentActivity

open class CommonActivity: ComponentActivity() {
  fun logi(text: String) {
    Log.i(this.packageName + "/" + this.localClassName, text)
  }
}