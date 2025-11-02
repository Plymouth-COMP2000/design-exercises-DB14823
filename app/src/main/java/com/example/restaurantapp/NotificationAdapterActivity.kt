package com.example.restaurantapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class NotificationAdapterActivity(
    private val notifications: MutableList<Notification>
) : RecyclerView.Adapter<NotificationAdapterActivity.NotificationViewHolder>() {

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.txtNotificationMessage)
        val time: TextView = itemView.findViewById(R.id.txtNotificationTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.message.text = notifications[position].message
        holder.time.text = notifications[position].time
    }

    override fun getItemCount(): Int = notifications.size

    fun deleteItem(position: Int) {
        notifications.removeAt(position)
        notifyItemRemoved(position)
    }
}

