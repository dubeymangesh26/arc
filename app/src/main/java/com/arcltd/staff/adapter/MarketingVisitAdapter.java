package com.arcltd.staff.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.arcltd.staff.R;
import com.arcltd.staff.activity.attendance.MarketingVisitDetailsActivity;
import com.arcltd.staff.response.MarketingVisitListResponse;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MarketingVisitAdapter extends RecyclerView.Adapter<MarketingVisitAdapter.ViewHolder> {

    private final Context context;
    private final List<MarketingVisitListResponse.MarketingVisitItem> list;

    public MarketingVisitAdapter(Context context, List<MarketingVisitListResponse.MarketingVisitItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_marketing_visit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list == null || position < 0 || position >= list.size()) return;

        MarketingVisitListResponse.MarketingVisitItem item = list.get(position);
        if (item == null) return;

        holder.tvCompanyName.setText(!TextUtils.isEmpty(item.getCompanyName()) ? item.getCompanyName() : "Company");

        String empName = !TextUtils.isEmpty(item.getEmpName()) ? item.getEmpName() : "Employee";
        String empCode = !TextUtils.isEmpty(item.getEmpCode()) ? item.getEmpCode() : "";
        holder.tvEmpInfo.setText(String.format("Visited By: %s (%s)", empName, empCode));

        String date = !TextUtils.isEmpty(item.getVisitDate()) ? item.getVisitDate() : "";
        String time = !TextUtils.isEmpty(item.getVisitTime()) ? item.getVisitTime() : "";
        holder.tvVisitDateTime.setText(String.format("%s %s", date, time).trim());

        if (!TextUtils.isEmpty(item.getImageUrl())) {
            Glide.with(context)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.person_24)
                    .error(R.drawable.avatar_10)
                    .into(holder.ivVisitImage);
        } else {
            holder.ivVisitImage.setImageResource(R.drawable.person_24);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MarketingVisitDetailsActivity.class);
            intent.putExtra("company_name", item.getCompanyName());
            intent.putExtra("emp_name", item.getEmpName());
            intent.putExtra("emp_code", item.getEmpCode());
            intent.putExtra("branch_code", item.getBranchCode());
            intent.putExtra("purpose", item.getPurpose());
            intent.putExtra("location_name", item.getLocationName());
            intent.putExtra("latitude", item.getLatitude());
            intent.putExtra("longitude", item.getLongitude());
            intent.putExtra("visit_date", item.getVisitDate());
            intent.putExtra("visit_time", item.getVisitTime());
            intent.putExtra("image_url", item.getImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCompanyName, tvEmpInfo, tvVisitDateTime;
        CircleImageView ivVisitImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCompanyName = itemView.findViewById(R.id.tv_company_name);
            tvEmpInfo = itemView.findViewById(R.id.tv_emp_info);
            tvVisitDateTime = itemView.findViewById(R.id.tv_visit_date_time);
            ivVisitImage = itemView.findViewById(R.id.iv_visit_image);
        }
    }
}
