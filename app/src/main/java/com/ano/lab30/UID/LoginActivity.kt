package com.ano.lab30.UID

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ano.lab30.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun toRegister(view: View){
        intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}