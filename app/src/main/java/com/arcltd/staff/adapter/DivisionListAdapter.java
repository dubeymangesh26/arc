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
import com.arcltd.staff.activity.listData.AdminBranchListActivity;
import com.arcltd.staff.response.DivisionListResponse;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DivisionListAdapter extends RecyclerView.Adapter<DivisionListAdapter.ViewHolder> {
    Context context;
    List<DivisionListResponse.DivisionList> list;
    RecyclerView menuList;
    private DivisionListResponse wishList;
    private int deliveryPosition;


    public DivisionListAdapter(FragmentActivity activity, List<DivisionListResponse.DivisionList>
            bookingLists, RecyclerView menuList, DivisionListResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_region_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.region.setText(list.get(position).getDivision_name());

        holder.listRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = new Gson().toJson(list.get(position));
                Log.e("TAG", "MessOfficeGodownList: "+data );
                context.startActivity(new Intent(context, AdminBranchListActivity.class)
                        .putExtra("div_id", list.get(position).getDiv_id())
                        .putExtra("A","D"));

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
        TextView region;
        LinearLayout listRegion;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            region = itemView.findViewById(R.id.region);
            listRegion = itemView.findViewById(R.id.listRegion);



        }
    }

}
