package com.ano.lab30.Message

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.ano.lab30.R
import com.ano.lab30.UID.UserData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.usersrow.view.*

class UsersAdapter(private val context:Context, var users:List<UserData>) : RecyclerView.Adapter<UserViewHolder>() {
    private val TAG = "UserAdapter"

    companion object{
        val USER_KEY="USER_KEY"
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.usersrow,parent,false))
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        var user = users[position]
        holder.username.text=user.nickname
        Picasso.get().load(user.profileImageView).into(holder.userImage)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatLogActivity::class.java)
            intent.putExtra(USER_KEY, user)
            Log.d(TAG, "${user.uid}")
            startActivity(context,intent,null)
        }
    }

}

class UserViewHolder(view: View):RecyclerView.ViewHolder(view) {
    var userImage = view.imageview_user_new_message
    var username = view.text_user_new_message
}


