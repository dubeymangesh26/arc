package com.arcltd.staff.authentication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.response.UnlockRequestListResponse;

import java.util.List;

public class DeviceUnlockAdapter extends RecyclerView.Adapter<DeviceUnlockAdapter.ViewHolder> {

    private List<UnlockRequestListResponse.UnlockRequestModel> dataList;
    private OnApproveClickListener listener;

    public interface OnApproveClickListener {
        void onApproveClick(UnlockRequestListResponse.UnlockRequestModel model);
    }

    public DeviceUnlockAdapter(List<UnlockRequestListResponse.UnlockRequestModel> dataList, OnApproveClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device_unlock_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UnlockRequestListResponse.UnlockRequestModel model = dataList.get(position);
        holder.tvEmpName.setText(model.getEmp_name());
        holder.tvEmpCode.setText(model.getEmp_code());
        holder.tvRequestDate.setText(model.getRequest_date());

        holder.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onApproveClick(model);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmpName, tvEmpCode, tvRequestDate;
        Button btnApprove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmpName = itemView.findViewById(R.id.tvEmpName);
            tvEmpCode = itemView.findViewById(R.id.tvEmpCode);
            tvRequestDate = itemView.findViewById(R.id.tvRequestDate);
            btnApprove = itemView.findViewById(R.id.btnApprove);
        }
    }
}