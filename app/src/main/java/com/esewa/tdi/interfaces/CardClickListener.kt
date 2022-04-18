package com.esewa.tdi.interfaces

import com.esewa.tdi.data.User

interface CardClickListener {

    fun onItemClick(user: User)
}