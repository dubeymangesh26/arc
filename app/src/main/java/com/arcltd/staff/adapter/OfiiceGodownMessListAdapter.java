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
import com.arcltd.staff.activity.detailsData.DetailsActivity;
import com.arcltd.staff.response.OfficeGodownMessListResponse;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OfiiceGodownMessListAdapter extends RecyclerView.Adapter<OfiiceGodownMessListAdapter.ViewHolder> {
    Context context;
    List<OfficeGodownMessListResponse.OfficeMessRentList> list;
    RecyclerView menuList;
    private OfficeGodownMessListResponse wishList;
    private int deliveryPosition;


    public OfiiceGodownMessListAdapter(FragmentActivity activity,
                                       List<OfficeGodownMessListResponse.OfficeMessRentList>
            bookingLists, RecyclerView menuList, OfficeGodownMessListResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_mess_gdn_office_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.region.setText(list.get(position).getSub_branch());
        holder.ofsGdn.setText(list.get(position).getOff_gdn_residence());
        holder.messAddress.setText(list.get(position).getLocation_address());

        holder.listGdnOffRES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = new Gson().toJson(list.get(position));
                Log.e("TAG", "saddLayoutCallBack: "+data );
                context.startActivity(new Intent(context, DetailsActivity.class).putExtra("data", data));

            }
        });


    }
    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface ListCallback {
    }

    public interface RerfreshWishList {
        void refresh(Boolean b);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView region,ofsGdn,messAddress;
        LinearLayout listGdnOffRES;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            region = itemView.findViewById(R.id.region);
            ofsGdn = itemView.findViewById(R.id.ofsGdn);
            messAddress = itemView.findViewById(R.id.messAddress);
            listGdnOffRES = itemView.findViewById(R.id.listGdnOffRES);



        }
    }

}
