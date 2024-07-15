package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button


class MainActivity : CommonActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)
    val button = findViewById<View>(R.id.button) as Button
    button?.setOnClickListener() {
      // displaying a toast message
      //Toast.makeText(this@MainActivity, R.string.message, Toast.LENGTH_LONG).show()
      logi(resources.getString(R.string.message))
      startActivity(Intent(this@MainActivity, WebViewActivity::class.java))
    }

  }
}
