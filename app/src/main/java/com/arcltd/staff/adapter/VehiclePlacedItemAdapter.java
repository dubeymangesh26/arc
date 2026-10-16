package com.arcltd.staff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.response.VehiclePlacedItemListResponse;
import com.arcltd.staff.response.VehiclePlacedListResponse;
import com.arcltd.staff.web_view.WebViewpoppupActivity;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VehiclePlacedItemAdapter extends RecyclerView.Adapter<VehiclePlacedItemAdapter.ViewHolder> {
    Context context;
    List<VehiclePlacedItemListResponse.PlaceNo> list;
    RecyclerView menuList;
    private VehiclePlacedItemListResponse wishList;
    private int deliveryPosition;


    public VehiclePlacedItemAdapter(FragmentActivity activity, List<VehiclePlacedItemListResponse.PlaceNo>
            bookingLists, RecyclerView menuList, VehiclePlacedItemListResponse activeRefreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRefreshWishList;
        this.list = bookingLists;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_vehicle, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.tvVehicleType.setText(list.get(position).getVehicle_type());
        holder.tvNoOfVehicle.setText("No Of Vehicle : "+list.get(position).getNoOfVehicle());
        holder.tvDestination.setText("Destination : "+list.get(position).getEtDestination());
        holder.tvDate.setText(list.get(position).getDate());

            /*holder.listVehicle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String data = new Gson().toJson(list.get(position));
                    Log.e("TAG", "VehicleList: "+data );

                    context.startActivity(new Intent(context, WebViewpoppupActivity.class)
                            .putExtra("data", data));

                }
            });*/

        }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface ListCallback {
    }

    public interface RefreshWishList {
        void refresh(Boolean b);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvVehicleType,tvNoOfVehicle,tvDestination,tvDate;
        LinearLayout listVehicle;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvVehicleType = itemView.findViewById(R.id.tvVehicleType);
            tvNoOfVehicle = itemView.findViewById(R.id.tvNoOfVehicle);
            listVehicle = itemView.findViewById(R.id.listVehicle);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvDate = itemView.findViewById(R.id.tvDate);


        }
    }

}
