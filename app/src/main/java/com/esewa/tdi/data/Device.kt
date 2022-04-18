package com.esewa.tdi.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Device(
    var Name: String? = null,
    var id: String? = null,
    var Used: Boolean? = null
) : Parcelable {
    override fun toString(): String = Name ?: ""
}