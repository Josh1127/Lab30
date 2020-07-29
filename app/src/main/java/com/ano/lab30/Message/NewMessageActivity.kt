package com.ano.lab30.Message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ano.lab30.R
import com.ano.lab30.UID.UserData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title="Select User"
        recycler_newMessage.setHasFixedSize(true)
        fetchUser()
    }

    private fun fetchUser() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                val users= ArrayList<UserData>()
                snapshot.children.forEach {
                    val user = it.getValue(UserData::class.java)
                    if (user != null){
                        users.add(user)
                    }
                }
                recycler_newMessage.adapter= UsersAdapter(users)
            }
        })
    }
}