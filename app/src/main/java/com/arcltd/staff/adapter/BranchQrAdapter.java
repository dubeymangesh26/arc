package com.arcltd.staff.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.response.BranchQrListResponse;

import java.util.List;
import java.util.Locale;

public class BranchQrAdapter extends RecyclerView.Adapter<BranchQrAdapter.ViewHolder> {

    public interface OnDeleteClickListener {
        void onDeleteClick(BranchQrListResponse.BranchQrItem item, int position);
    }

    private final Context context;
    private final List<BranchQrListResponse.BranchQrItem> list;
    private final OnDeleteClickListener deleteClickListener;

    public BranchQrAdapter(Context context, List<BranchQrListResponse.BranchQrItem> list, OnDeleteClickListener listener) {
        this.context = context;
        this.list = list;
        this.deleteClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_branch_qr, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list == null || position < 0 || position >= list.size()) return;

        BranchQrListResponse.BranchQrItem item = list.get(position);
        if (item == null) return;

        String name = !TextUtils.isEmpty(item.getBranchName()) ? item.getBranchName() : "Branch";
        String code = !TextUtils.isEmpty(item.getBranchCode()) ? item.getBranchCode() : "";
        holder.tvBranchTitle.setText(String.format("%s (%s)", name, code));

        holder.tvLatLong.setText(String.format(Locale.getDefault(), "📍 Lat: %.6f | Lng: %.6f", item.getLatitude(), item.getLongitude()));
        holder.tvRadius.setText(String.format(Locale.getDefault(), "🎯 Geofence Radius: %d meters", item.getRadius() > 0 ? item.getRadius() : 150));

        String qrKey = !TextUtils.isEmpty(item.getQrKey()) ? item.getQrKey() : ("ARC_OFFICE_QR_" + code);
        holder.tvQrKey.setText(String.format("🔑 Payload: %s", qrKey));

        holder.btnDeleteQr.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                int pos = holder.getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    deleteClickListener.onDeleteClick(item, pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBranchTitle, tvLatLong, tvRadius, tvQrKey;
        Button btnDeleteQr;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBranchTitle = itemView.findViewById(R.id.tv_branch_title);
            tvLatLong = itemView.findViewById(R.id.tv_lat_long);
            tvRadius = itemView.findViewById(R.id.tv_radius);
            tvQrKey = itemView.findViewById(R.id.tv_qr_key);
            btnDeleteQr = itemView.findViewById(R.id.btn_delete_qr);
        }
    }
}
