package com.ano.lab30.UID

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserData(var uid: String, var nickname: String, var profileImageView:String) : Parcelable {
    constructor():this("","","")
}
