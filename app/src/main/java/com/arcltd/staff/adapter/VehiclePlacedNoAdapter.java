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
import com.arcltd.staff.response.VehiclePlacedListResponse;
import com.arcltd.staff.web_view.WebViewpoppupActivity;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VehiclePlacedNoAdapter extends RecyclerView.Adapter<VehiclePlacedNoAdapter.ViewHolder> {
    Context context;
    List<VehiclePlacedListResponse.PlaceNo> list;
    RecyclerView menuList;
    private VehiclePlacedListResponse wishList;
    private int deliveryPosition;


    public VehiclePlacedNoAdapter(FragmentActivity activity, List<VehiclePlacedListResponse.PlaceNo>
            bookingLists, RecyclerView menuList, VehiclePlacedListResponse activeRefreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRefreshWishList;
        this.list = bookingLists;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_vehicle_no, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.tvBranchName.setText(list.get(position).getDivision_Name());
        holder.tvNoOfVehicle.setText(list.get(position).getNo_of_Vehicle());

         /*   holder.listVehicle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String data = new Gson().toJson(list.get(position));
                    Log.e("TAG", "VehicleList: "+data );

                    context.startActivity(new Intent(context, WebViewpoppupActivity.class)
                            .putExtra("data", data));

                }
            });
*/
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
        TextView tvBranchName,tvNoOfVehicle;
        LinearLayout listVehicle;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvBranchName = itemView.findViewById(R.id.tvBranchName);
            tvNoOfVehicle = itemView.findViewById(R.id.tvNoOfVehicle);
            listVehicle = itemView.findViewById(R.id.listVehicle);


        }
    }

}
