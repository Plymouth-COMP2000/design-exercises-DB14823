package com.example.restaurantapp.ui.Customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantapp.R;
import com.example.restaurantapp.model.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingVH> {

    public interface OnDeleteClickListener {
        void onDeleteClicked(Booking booking);
    }

    private final List<Booking> data = new ArrayList<>();
    private final OnDeleteClickListener deleteListener;

    public BookingsAdapter(OnDeleteClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public void setData(List<Booking> newData) {
        data.clear();
        if (newData != null) data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookingVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_row, parent, false); // <-- change filename if needed
        return new BookingVH(row);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingVH holder, int position) {
        Booking b = data.get(position);

        holder.txtDate.setText(b.date);
        holder.txtTime.setText(b.time);

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) deleteListener.onDeleteClicked(b);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class BookingVH extends RecyclerView.ViewHolder {
        TextView txtDate;
        TextView txtTime;
        ImageView btnDelete;

        BookingVH(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtBookingDate);
            txtTime = itemView.findViewById(R.id.txtBookingTime);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
