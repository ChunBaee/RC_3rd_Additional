package com.jcorp.exams

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getPrefs()
    }

    private fun getPrefs() {
        val prefs = getSharedPreferences("UserData", MODE_PRIVATE)
        val inputText = prefs.getString("Email", "")
        if(inputText?.isEmpty() == true) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}