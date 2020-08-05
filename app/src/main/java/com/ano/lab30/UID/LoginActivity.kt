package com.ano.lab30.UID

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ano.lab30.Message.LatestMessageActivity
import com.ano.lab30.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    companion object{
        private val TAG= LoginActivity::class.java.simpleName
        private const val RC_SIGN_IN = 100
    }

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
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
        createSignInIntent()

/*        intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)*/
    }

    private fun createSignInIntent() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setAlwaysShowSignInMethodScreen(true)
                .setIsSmartLockEnabled(false)
                .setTosAndPrivacyPolicyUrls(
                    "https://www.google.com/服務條款",
                    "https://www.youtube.com/隱私權政策")
                .setTheme(R.style.LoginTheme)
                .build(),
            RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != Activity.RESULT_OK) {
                val response = IdpResponse.fromResultIntent(data)
                Toast.makeText(applicationContext, response?.error?.errorCode.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun changePassword(view: View){
        val emailAddress = login_email.text.toString()
        if (emailAddress.isNotEmpty()) {
            firebaseAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "需求已送出", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "email傳送失敗", Toast.LENGTH_LONG).show()
                }
            }
        }else{
            Toast.makeText(this, "請輸入email", Toast.LENGTH_LONG).show()
        }

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