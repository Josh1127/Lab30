package com.ano.lab30.Message

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import com.ano.lab30.Message.UsersAdapter.Companion.USER_KEY
import com.ano.lab30.R
import com.ano.lab30.R.id.*
import com.ano.lab30.UID.LoginActivity
import com.ano.lab30.UID.UserData
import com.ano.lab30.UID.UserDataActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_latestmessage.*

class LatestMessageActivity : AppCompatActivity() {
    companion object{
        var currentUser:UserData?= null
        private val TAG=LatestMessageActivity::class.java.simpleName
    }

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    val adapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latestmessage)

        recyclerView_latestMessage.adapter=adapter

        recyclerView_latestMessage
            .addItemDecoration(
                DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL)
            )

        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this, ChatLogActivity::class.java)
            val row = item as LatestMessageRow
            intent.putExtra(USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }

        bottom_nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                userIcon-> startActivity(Intent(this, UserDataActivity::class.java))
                new_message-> startActivity(Intent(this, NewMessageActivity::class.java))
                sign_out-> {
                    firebaseAuth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
            true
        }

        fetchCurrentUser()

        listenLatestMessage()
    }

    private val latestMessageMap= HashMap<String, ChatMessage>()
    private fun refreshRecyclerViewMessage() {
        adapter.clear()
        latestMessageMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }

    private fun listenLatestMessage() {
        val fromId=FirebaseAuth.getInstance().uid
        Log.d(TAG, "listenLatestMessage: $fromId")
        val ref = FirebaseDatabase.getInstance().getReference("latest-message/$fromId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                Log.d(TAG, "onChildChanged: $chatMessage")
                latestMessageMap[snapshot.key!!]= chatMessage
                refreshRecyclerViewMessage()
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                adapter.add(LatestMessageRow(chatMessage))
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }



    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(UserData::class.java)
            }
        })
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