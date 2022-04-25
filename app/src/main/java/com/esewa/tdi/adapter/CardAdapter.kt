package com.esewa.tdi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.esewa.tdi.data.User
import com.esewa.tdi.interfaces.CardClickListener
import com.esewa.tdi.util.gone
import com.esewa.tdi.util.visible
import esewa.tdi.databinding.UserItemBinding
import java.text.SimpleDateFormat
import java.util.*

class CardAdapter(
    private var userList: List<User>,
    val listener: CardClickListener
) : RecyclerView.Adapter<CardAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class MyViewHolder(
        private val binding: UserItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) = with(binding) {
            fullName.text = user.Name
            deviceName.text = user.Device
            /*val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.getDefault())
           *//* user.date?.let {
                if (it > 0) {
                    assigendDate.visible()
                    assigendDate.text = sdf.format(Date(it))
                } else {
                    assigendDate.gone()
                }
            } ?: run {
                assigendDate.gone()
            }*/

            itemView.setOnClickListener { listener.onItemClick(user) }
        }
    }
}