package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast


class WebViewActivity : CommonActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val myWebView = WebView(this.applicationContext)
//    myWebView.setWebContentsDebuggingEnabled(true)
    // Seems needed for alerts https://stackoverflow.com/questions/5271898/javascript-alert-not-working-in-android-webview
    myWebView.webChromeClient = object : WebChromeClient() {
      override fun onJsAlert(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
      ): Boolean {
        logi("alert($message)")
        Toast.makeText(this@WebViewActivity, message, Toast.LENGTH_LONG).show()
        // cr_WebViewCallback: Unable to create JsDialog without an Activity
        return super.onJsAlert(view, url, message, result)
      }
      // Needed for debugging with adb https://developer.android.com/develop/ui/views/layout/webapps/debugging
      override fun onConsoleMessage(message: ConsoleMessage): Boolean {
        logi("${message.message()} -- From line " +
          "${message.lineNumber()} of ${message.sourceId()}")
        return true
      }
    }
    val settings: WebSettings = myWebView.settings

    settings.allowFileAccess = true
    settings.javaScriptEnabled = true
    settings.domStorageEnabled = true
    // For testing. Need to test from an http URL
    settings.allowFileAccessFromFileURLs = true
    //settings.allowUniversalAccessFromFileURLs = true

    //settings.javaScriptCanOpenWindowsAutomatically = true
    setContentView(myWebView)

    // TEST 1: set the baseURL to resource we want to load, so that CORS allows it.
    // val htmlStr = "" +
    //   "<html>\n" +
    //   " <head>\n" +
    //   " </head>\n" +
    //   " <body>\n" +
    //   "\n" +
    //   " <script>\n" +
    //   "<!--   alert(\"here\")-->\n" +
    //   "   var xhr = new XMLHttpRequest();\n" +
    //   "   xhr.onreadystatechange = function() {\n" +
    //   "     if (xhr.readyState == XMLHttpRequest.DONE) {\n" +
    //   "        alert(xhr.responseText);\n" +
    //   "        }\n" +
    //   "   }\n" +
    //   "<!--   xhr.open('GET', 'file:///data/data/com.example.myapplication/hello.html', true);-->\n" +
    //   "  alert('file:///data/data/com.example.myapplication/secret')\n" +
    //   "    xhr.open('GET', 'file:///data/data/com.example.myapplication/secret', true);\n" +
    //   "   xhr.send(null);\n" +
    //   "    let d = new Date();\n" +
    //   "<!--    alert(\"Today's date is \" + d);-->\n" +
    //   "<!--    console.log(\"Hello world \" + d);-->\n" +
    //   " </script>\n" +
    //   "\n" +
    //   "    <b>Hello World</b>\n" +
    //   "    <p>Hello World</p>\n" +
    //   "     Hello World     \n" +
    //   " </body>\n" +
    //   "</html>\n"
    // myWebView.loadDataWithBaseURL( "file:///data/data/com.example.myapplication", htmlStr, "text/html", "UTF-8", "");

    // TEST2: File loads another file
    // Works if we allow files to load other files via CORS with allowFileAccessFromFileURLs = true
    // Attack would get vulnerable app to load a local file of its choice
    myWebView.loadUrl("file:///data/data/com.example.myapplication/hello.html")

    // TEST3: http origin load a file - never works
    // adb reverse tcp:8000 tcp:8000
    // python3 -m http.server 8000
    // ERROR: Not allowed to load local resource: file:///data/data/com.example.myapplication/hello.html -- From line 15 of http://localhost:8000/hello.html
    //myWebView.loadUrl("http://localhost:8000/hello.html")

  // val unencodedHtml =
    //   "<html><body>'%23' is the percent code for ‘#‘ </body></html>";
    // val encodedHtml = Base64.encodeToString(unencodedHtml.toByteArray(), Base64.NO_PADDING)
    // myWebView.loadData(encodedHtml, "text/html", "base64")
  }
}
