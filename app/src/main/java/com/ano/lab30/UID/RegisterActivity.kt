package com.ano.lab30.UID

import android.content.DialogInterface
import android.content.DialogInterface.*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.ano.lab30.Message.LatestMessageActivity
import com.ano.lab30.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    companion object{
        val TAG = RegisterActivity::class.java.simpleName
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }
    fun register(view: View){
        val email = register_email.text.toString().trim()
        val passwd = register_passwd.text.toString().trim()
        val check = passwd_check.text.toString().trim()
        if (email.isEmpty() || passwd.isEmpty()){
            Toast.makeText(this, "請輸入帳號及密碼", Toast.LENGTH_LONG).show()
            return
        }else if (passwd == check){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, passwd)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(TAG, "Successfully create user with UID" + it.result?.user?.uid)
                        createUserOnFirebaseDatabase()
                    }else{
                        Log.d(TAG, "register: is failure")
                    }
                }
        }else{
            Toast.makeText(this, "密碼錯誤",  Toast.LENGTH_SHORT).show()
        }
    }

    private fun createUserOnFirebaseDatabase() {
        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = NewUser(uid, register_email.text.toString())
        ref.setValue(user)
            .addOnCompleteListener {
                Log.d(TAG, "User database have been save to Firebase database")
                AlertDialog.Builder(this)
                    .setTitle("註冊結果")
                    .setMessage("註冊成功")
                    .setPositiveButton("OK"
                    ) { _, _ -> startActivity(Intent(this, LatestMessageActivity::class.java))}.show()
            }
    }


}