package com.ano.lab30.Message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ano.lab30.R
import com.ano.lab30.UID.UserData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.usersrow.view.*

class UsersAdapter(var users:List<UserData>) : RecyclerView.Adapter<UserViewHolder>() {
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
    }

}

class UserViewHolder(view: View):RecyclerView.ViewHolder(view) {
    var userImage = view.imageview_user_new_message
    var username = view.text_user_new_message
}


