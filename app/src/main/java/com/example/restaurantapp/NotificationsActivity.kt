package com.example.restaurantapp

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class Notification(val message: String, val time: String)

class NotificationsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotificationAdapter
    private val notifications = mutableListOf(
        Notification("Your table is booked for 6 PM tonight", "1h ago"),
        Notification("Booking confirmed for 12th Nov at 7 PM", "2 days ago"),
        Notification("Special deal: 20% off on desserts today!", "3 days ago")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        val backButton: ImageView = findViewById(R.id.btnBack)
        backButton.setOnClickListener { finish() }

        recyclerView = findViewById(R.id.recyclerNotifications)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = NotificationAdapter(notifications)
        recyclerView.adapter = adapter

        // ✅ Add spacing between each notification bubble
        val spacing = (12 * resources.displayMetrics.density).toInt() // 12dp
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.bottom = spacing
            }
        })

        // ✅ Swipe-to-delete
        val swipeToDelete = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                notifications.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
        }

        ItemTouchHelper(swipeToDelete).attachToRecyclerView(recyclerView)
    }
}

// ✅ RecyclerView Adapter for notifications
class NotificationAdapter(private val notificationList: List<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.txtNotificationMessage)
        val time: TextView = itemView.findViewById(R.id.txtNotificationTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]
        holder.message.text = notification.message
        holder.time.text = notification.time
    }

    override fun getItemCount(): Int = notificationList.size
}
