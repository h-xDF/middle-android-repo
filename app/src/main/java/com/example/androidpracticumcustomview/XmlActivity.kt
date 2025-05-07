package com.example.androidpracticumcustomview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.androidpracticumcustomview.ui.theme.CustomContainer


class XmlActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startXmlPracticum()
    }

    private fun startXmlPracticum() {

        val customContainer = CustomContainer(this)
        setContentView(customContainer)
        // test for xml example
        //setContentView(R.layout.xml_activity)

        customContainer.setOnClickListener {
            finish()
        }

        val firstView = TextView(this).apply {
            text = context.getString(R.string.text_res_1)
        }

        val secondView = TextView(this).apply {
            text = context.getString(R.string.text_res_2)
        }

        customContainer.addView(firstView)
        // Добавление второго элемента через некоторое время (например, по задержке)
        Handler(Looper.getMainLooper()).postDelayed({
            customContainer.addView(secondView)
        }, 2000)
    }
}