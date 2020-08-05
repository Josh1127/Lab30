
package com.ano.lab30.UID

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.ano.lab30.Message.LatestMessageActivity
import com.ano.lab30.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_user_data.*
import java.util.*

class UserDataActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "UserDataActivity"
    }
    private val firebaseAuth = FirebaseAuth.getInstance()

    private var userPhotoPickUri : Uri? = null

    private var userName = firebaseAuth.currentUser?.displayName ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_data)

        nickname.setText(userName)
    }

    fun photoPick(view: View){
        intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, 30)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==30 && resultCode == Activity.RESULT_OK && data != null){
            userPhotoPickUri = data.data
            imageView_user.setImageURI(userPhotoPickUri)
            textView3.alpha=0f
        }
    }

    fun save(view: View){
        if(userPhotoPickUri == null)  return
        val filename =UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(userPhotoPickUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    saveUserToDatabase(it.toString())
                }
            }
    }

    private fun saveUserToDatabase(profileImageUri:String) {
        val uid = firebaseAuth.uid ?:""
        val user = UserData(uid,nickname.text.toString(), profileImageUri)
        val database = FirebaseDatabase.getInstance().getReference("/users/$uid/")
        database.setValue(user)
            .addOnSuccessListener {
                intent = Intent(this, LatestMessageActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }
}