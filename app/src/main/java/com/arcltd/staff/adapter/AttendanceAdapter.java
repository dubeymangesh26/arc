package com.arcltd.staff.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.attendance.EmpAttendanceMonthlyActivity;
import com.arcltd.staff.response.AttendanceReportResponse;

import java.util.List;
import java.util.Locale;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private final Context context;
    private final List<AttendanceReportResponse.AttendanceItem> list;

    public AttendanceAdapter(Context context, List<AttendanceReportResponse.AttendanceItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attendance_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list == null || position < 0 || position >= list.size()) return;

        AttendanceReportResponse.AttendanceItem item = list.get(position);
        if (item == null) return;

        String name = !TextUtils.isEmpty(item.getEmpName()) ? item.getEmpName() : "Employee";
        String code = !TextUtils.isEmpty(item.getEmpCode()) ? item.getEmpCode() : "";
        holder.tvEmpName.setText(String.format("%s (%s)", name, code));

        String branchName = !TextUtils.isEmpty(item.getBranchName()) ? item.getBranchName() : "Branch";
        String branchCode = !TextUtils.isEmpty(item.getBranchCode()) ? item.getBranchCode() : "";
        holder.tvBranch.setText(String.format("Branch: %s (%s)", branchName, branchCode));

        String status = item.getStatus();
        if (TextUtils.isEmpty(status)) {
            if (!TextUtils.isEmpty(item.getTimeIn()) && !"-".equals(item.getTimeIn().trim())) {
                status = "PRESENT";
            } else {
                status = "ABSENT";
            }
        }
        status = status.toUpperCase();

        holder.tvStatus.setText(status);
        if ("PRESENT".equalsIgnoreCase(status)) {
            holder.tvStatus.getBackground().setTint(Color.parseColor("#25D366"));
        } else {
            holder.tvStatus.getBackground().setTint(Color.parseColor("#FF3B30"));
        }

        holder.tvTimeIn.setText(String.format("Time In: %s", !TextUtils.isEmpty(item.getTimeIn()) ? item.getTimeIn() : "-"));
        holder.tvTimeOut.setText(String.format("Time Out: %s", !TextUtils.isEmpty(item.getTimeOut()) ? item.getTimeOut() : "-"));

        if (item.getDistanceMeters() > 0 && "PRESENT".equalsIgnoreCase(status)) {
            holder.tvDistance.setText(String.format(Locale.getDefault(), "Verified Location: %.1fm from office", item.getDistanceMeters()));
            holder.tvDistance.setVisibility(View.VISIBLE);
        } else {
            holder.tvDistance.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EmpAttendanceMonthlyActivity.class);
            intent.putExtra("emp_code", item.getEmpCode());
            intent.putExtra("emp_name", item.getEmpName());
            intent.putExtra("branch_name", item.getBranchName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvEmpName, tvBranch, tvStatus, tvTimeIn, tvTimeOut, tvDistance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmpName = itemView.findViewById(R.id.tv_item_emp_name);
            tvBranch = itemView.findViewById(R.id.tv_item_branch);
            tvStatus = itemView.findViewById(R.id.tv_item_status);
            tvTimeIn = itemView.findViewById(R.id.tv_item_time_in);
            tvTimeOut = itemView.findViewById(R.id.tv_item_time_out);
            tvDistance = itemView.findViewById(R.id.tv_item_distance);
        }
    }
}
