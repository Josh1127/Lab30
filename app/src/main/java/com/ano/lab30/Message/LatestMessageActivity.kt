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
    val firebaseAuth = FirebaseAuth.getInstance()

    val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }else{
            Log.d(TAG, "Current user"+firebaseUser.uid)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latestmessage)
    }


    override fun onStart() {
        super.onStart()
        firebaseAuth!!.addAuthStateListener(this.authStateListener!!)

    }

    override fun onStop() {
        super.onStop()
        firebaseAuth!!.removeAuthStateListener(this.authStateListener!!)

    }
}