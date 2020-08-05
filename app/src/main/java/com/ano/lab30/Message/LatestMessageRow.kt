package com.ano.lab30.Message

import android.util.Log
import com.ano.lab30.R
import com.ano.lab30.UID.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessageRow(private var chatMessage: ChatMessage) : Item<GroupieViewHolder>(){
    var chatPartnerUser : UserData?= null
    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txt_latest_message.text=chatMessage.text
        Log.d("LatestMessageRow", "bind: ${chatMessage.text}")
        val chatPartnerId:String = if (chatMessage.fromId == FirebaseAuth.getInstance().uid){
            chatMessage.toId
        }else{
            chatMessage.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                chatPartnerUser = snapshot.getValue(UserData::class.java) ?:return
                viewHolder.itemView.txt_user_latestmessage.text=chatPartnerUser?.nickname
                Log.d("LatestMessageRow", "onDataChange: ${chatPartnerUser?.nickname}")
                val targetImageView = viewHolder.itemView.image_latetmessage_row
                Picasso.get().load(chatPartnerUser?.profileImageView).into(targetImageView)
            }
        })
    }

}
