package com.example.restaurantapp.ui.Staff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantapp.R;
import com.example.restaurantapp.model.Booking;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StaffBookingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_BOOKING = 1;

    public interface OnDeleteClickListener {
        void onDeleteClicked(Booking booking);
    }

    private final List<Row> rows = new ArrayList<>();
    private final OnDeleteClickListener deleteListener;

    public StaffBookingsAdapter(OnDeleteClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }


    public void setBookings(List<Booking> bookings) {
        rows.clear();

        String lastDate = null;
        if (bookings != null) {
            for (Booking b : bookings) {
                if (b == null) continue;

                if (lastDate == null || !lastDate.equals(b.date)) {
                    rows.add(Row.header(b.date));
                    lastDate = b.date;
                }
                rows.add(Row.booking(b));
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return rows.get(position).type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.booking_day_header, parent, false);
            return new HeaderVH(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.booking_item_staff, parent, false);
            return new BookingVH(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Row row = rows.get(position);

        if (holder instanceof HeaderVH) {
            ((HeaderVH) holder).txtDayHeader.setText(formatDateHeader(row.date));
        } else if (holder instanceof BookingVH) {
            Booking b = row.booking;

            BookingVH vh = (BookingVH) holder;
            vh.txtUser.setText("Booked by: " +
                    (b.displayName == null || b.displayName.isEmpty()
                            ? b.username
                            : b.displayName));
            vh.txtParty.setText("Party size: " + b.partySize);
            vh.txtTime.setText("Time: " + b.time);
            vh.btnDelete.setOnClickListener(v -> {
                if (deleteListener != null) deleteListener.onDeleteClicked(b);
            });
        }
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    static class HeaderVH extends RecyclerView.ViewHolder {
        TextView txtDayHeader;

        HeaderVH(@NonNull View itemView) {
            super(itemView);
            txtDayHeader = itemView.findViewById(R.id.txtDayHeader);
        }
    }

    static class BookingVH extends RecyclerView.ViewHolder {
        TextView txtUser;
        TextView txtDate;
        TextView txtTime;
        ImageView btnDelete;
        TextView txtParty;


        BookingVH(@NonNull View itemView) {
            super(itemView);
            txtUser = itemView.findViewById(R.id.txtBookingUser);
            txtDate = itemView.findViewById(R.id.txtBookingDate);
            txtTime = itemView.findViewById(R.id.txtBookingTime);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            txtParty = itemView.findViewById(R.id.txtBookingParty);
        }
    }


    private static class Row {
        int type;
        String date;
        Booking booking;

        static Row header(String date) {
            Row r = new Row();
            r.type = TYPE_HEADER;
            r.date = date;
            return r;
        }

        static Row booking(Booking b) {
            Row r = new Row();
            r.type = TYPE_BOOKING;
            r.booking = b;
            return r;
        }
    }

    private String formatDateHeader(String isoDate) {
        try {
            SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            SimpleDateFormat out = new SimpleDateFormat("EEEE d MMM yyyy", Locale.UK);
            return out.format(in.parse(isoDate));
        } catch (Exception e) {
            return isoDate;
        }
    }

    private String formatDateNice(String rawDate) {
        try {
            SimpleDateFormat dbFmt = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            SimpleDateFormat uiFmt = new SimpleDateFormat("EEE d MMM yyyy", Locale.UK);
            Date d = dbFmt.parse(rawDate);
            return uiFmt.format(d);
        } catch (Exception e) {
            return rawDate;
        }
    }

}
