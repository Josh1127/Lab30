package com.ano.lab30.Message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.ano.lab30.R
import com.ano.lab30.UID.UserData
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_row_guest.view.*
import kotlinx.android.synthetic.main.chat_row_user.view.*

class ChatLogActivity : AppCompatActivity() {
    private val TAG = ChatLogActivity::class.java.simpleName
    val adapter = GroupAdapter<GroupieViewHolder>()

    var toUser:UserData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recycler_chat_log.adapter=adapter

        toUser =intent.getParcelableExtra(UsersAdapter.USER_KEY)
        supportActionBar?.title=toUser?.nickname



        listenForMessage()
    }

    private fun listenForMessage() {
        val fromId=FirebaseAuth.getInstance().uid
        val toId= toUser?.uid
        val ref = FirebaseDatabase
            .getInstance()
            .getReference("/user-messages/$fromId/$toId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if (chatMessage != null){
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid){
                        val currentUser = LatestMessageActivity.currentUser ?:return
                        Log.d(TAG, "currentuser:\t ${currentUser.nickname}")
                        adapter.add(ChatFromItem(chatMessage.text, currentUser))
                    }else{
                        adapter.add(ChatToItem(chatMessage.text, toUser!!))
                    }
                }
                recycler_chat_log.scrollToPosition(adapter.itemCount-1)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}
        })
    }

    fun sendMessage(view: View){
        performSendingMessage()
    }

    private fun performSendingMessage() {
        val text = edittext_chat_log.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val user =intent.getParcelableExtra<UserData>(UsersAdapter.USER_KEY)
        val toId =user!!.uid
        if (fromId == null) return
        val reference = FirebaseDatabase
            .getInstance()
            .getReference("/user-messages/$fromId/$toId")
            .push()
        val toReference = FirebaseDatabase
            .getInstance()
            .getReference("/user-messages/$toId/$fromId")
            .push()
        val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, System.currentTimeMillis()/1000)
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message${reference.key}")
                edittext_chat_log.text.clear()
                recycler_chat_log.scrollToPosition(adapter.itemCount-1)
            }
        toReference.setValue(chatMessage)
        val latesMessageRef = FirebaseDatabase
            .getInstance()
            .getReference("latest-message/$fromId/$toId")
        latesMessageRef.setValue(chatMessage)

        val latesMessageToRef = FirebaseDatabase
            .getInstance()
            .getReference("latest-message/$toId/$fromId")
        latesMessageToRef.setValue(chatMessage)

    }
}


class ChatMessage(val id: String, val text: String, val fromId:String, val toId:String, val timestamp:Long){
    constructor() : this("","","","",-1)
}

class ChatFromItem(val text: String, private val userData: UserData) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_row_user
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.text_user_chat_log.text = text
        val uri = userData.profileImageView
        val targetImageView = viewHolder.itemView.imageview_user_chat_log
        Picasso.get().load(uri).into(targetImageView)
    }
}

class ChatToItem(val text: String, private val userData: UserData) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_row_guest
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.text_guest_chat_log.text = text
        val uri = userData.profileImageView
        val targetImageView = viewHolder.itemView.imageview_guest_chat_log
        Picasso.get().load(uri).into(targetImageView)
    }

}
