package com.esewa.tdi.data

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: String? = null,
    var Name: String? = null,
    var Device: String? = null,
    var Charger: String? = null,
    var Cable: String? = null
) : Parcelable {
    constructor()
            : this(null, null, null, null, null)

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to Name,
            "Device" to Device,
            "Charger" to Charger,
            "Cable" to Cable,
        )
    }

    override fun toString(): String = Name ?: ""
}