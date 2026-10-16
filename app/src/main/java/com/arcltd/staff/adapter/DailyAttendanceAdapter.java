package com.arcltd.staff.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.response.EmpMonthlyAttendanceResponse;

import java.util.List;

public class DailyAttendanceAdapter extends RecyclerView.Adapter<DailyAttendanceAdapter.ViewHolder> {

    private final Context context;
    private final List<EmpMonthlyAttendanceResponse.DailyItem> list;

    public DailyAttendanceAdapter(Context context, List<EmpMonthlyAttendanceResponse.DailyItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_daily_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list == null || position < 0 || position >= list.size()) return;

        EmpMonthlyAttendanceResponse.DailyItem item = list.get(position);
        if (item == null) return;

        holder.tvDate.setText(!TextUtils.isEmpty(item.getDate()) ? item.getDate() : "");

        String inStr = !TextUtils.isEmpty(item.getTimeIn()) ? item.getTimeIn() : "-";
        String outStr = !TextUtils.isEmpty(item.getTimeOut()) ? item.getTimeOut() : "-";
        holder.tvTime.setText(String.format("In: %s  |  Out: %s", inStr, outStr));

        String status = !TextUtils.isEmpty(item.getStatus()) ? item.getStatus().toUpperCase() : "ABSENT";

        if ("PRESENT".equalsIgnoreCase(status)) {
            holder.tvStatus.setText("✅ PRESENT");
            holder.tvStatus.getBackground().setTint(Color.parseColor("#25D366"));
            holder.cardDailyItem.setCardBackgroundColor(Color.parseColor("#E8F8EE"));
        } else {
            holder.tvStatus.setText("❌ ABSENT");
            holder.tvStatus.getBackground().setTint(Color.parseColor("#FF3B30"));
            holder.cardDailyItem.setCardBackgroundColor(Color.parseColor("#FDE8E8"));
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardDailyItem;
        TextView tvDate, tvTime, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardDailyItem = itemView.findViewById(R.id.card_daily_item);
            tvDate = itemView.findViewById(R.id.tv_daily_date);
            tvTime = itemView.findViewById(R.id.tv_daily_time);
            tvStatus = itemView.findViewById(R.id.tv_daily_status);
        }
    }
}
