package com.ano.lab30.Message

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ano.lab30.R
import com.ano.lab30.UID.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class LatestMessageActivity : AppCompatActivity() {
    companion object{
        private val REQUEST_CORD=10
        private val TAG=LatestMessageActivity::class.java.simpleName
    }

    private lateinit var mAuth: FirebaseAuth
    private var login = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latestmessage)
        if (!login){
            intent = Intent(this, LoginActivity::class.java)
            startActivityForResult(intent, REQUEST_CORD)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10 && resultCode != Activity.RESULT_OK){
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener {
            val user = it.currentUser
            if (user !=null){
                Log.d(TAG, "User is singed whit UID"+user.uid)
                login=true
            }
        }
    }
}