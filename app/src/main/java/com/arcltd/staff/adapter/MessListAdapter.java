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
import com.arcltd.staff.activity.messDetails.MessDetailsActivity;
import com.arcltd.staff.response.MessListResponse;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessListAdapter extends RecyclerView.Adapter<MessListAdapter.ViewHolder> {
    Context context;
    List<MessListResponse.MessList> list;
    RecyclerView menuList;
    private MessListResponse wishList;
    private int deliveryPosition;


    public MessListAdapter(FragmentActivity activity, List<MessListResponse.MessList>
            bookingLists, RecyclerView menuList, MessListResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_mess_list, parent, false);
        return new ViewHolder(view);



    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.region.setText(list.get(position).getMess_name());
        holder.messAddress.setText(list.get(position).getMess_address());

        holder.listRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = new Gson().toJson(list.get(position));
                Log.e("TAG", "MessList: "+data );
                context.startActivity(new Intent(context, MessDetailsActivity.class).putExtra("dataDetails", data));

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
        TextView region,messAddress;
        LinearLayout listRegion;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            region = itemView.findViewById(R.id.region);
            listRegion = itemView.findViewById(R.id.listRegion);
            messAddress = itemView.findViewById(R.id.messAddress);


        }
    }
}