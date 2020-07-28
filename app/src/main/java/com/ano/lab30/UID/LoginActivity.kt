package com.ano.lab30.UID

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ano.lab30.Message.LatestMessageActivity
import com.ano.lab30.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    companion object{
        private val TAG= LoginActivity::class.java.simpleName
    }

    val firebaseAuth = FirebaseAuth.getInstance()
    val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            val intent = Intent(this, LatestMessageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
    fun login(view: View){
        val email= login_email.text.toString()
        val passwd  = login_passwd.text.toString()
        if (email.isEmpty() || passwd.isEmpty()){
            Toast.makeText(this, "請輸入帳號及密碼", Toast.LENGTH_LONG).show()
            return
        }else{
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, passwd)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        startActivity(Intent(this, LatestMessageActivity::class.java))
                    }else{
                        Toast.makeText(this, "帳號密碼錯誤!", Toast.LENGTH_LONG).show()
                        login_email.setText("")
                        login_passwd.setText("")
                    }
                }

        }
    }

    fun toRegister(view: View){
        intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
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