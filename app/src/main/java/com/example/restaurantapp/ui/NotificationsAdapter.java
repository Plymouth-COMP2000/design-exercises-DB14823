package com.example.restaurantapp.ui;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantapp.R;
import com.example.restaurantapp.model.Notifications;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.VH> {

    public interface OnDeleteClick {
        void onDelete(Notifications n);
    }

    private final List<Notifications> data = new ArrayList<>();
    private final OnDeleteClick onDeleteClick;

    public NotificationsAdapter(OnDeleteClick onDeleteClick) {
        this.onDeleteClick = onDeleteClick;
    }

    public void setData(List<Notifications> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Notifications n = data.get(position);

        holder.txtMessage.setText(n.message);

        CharSequence relTime = DateUtils.getRelativeTimeSpanString(
                n.createdAt,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
        );
        holder.txtTime.setText(relTime);

        holder.btnDelete.setOnClickListener(v -> {
            if (onDeleteClick != null) onDeleteClick.onDelete(n);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView txtMessage;
        TextView txtTime;
        ImageView btnDelete;

        VH(@NonNull View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtNotificationMessage);
            txtTime = itemView.findViewById(R.id.txtNotificationTime);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
