package com.arcltd.staff.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.response.RegionListResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RegionListAdapter extends RecyclerView.Adapter<RegionListAdapter.ViewHolder> {
    Context context;
    List<RegionListResponse.RegionList> list;
    RecyclerView menuList;
    private RerfreshWishList wishList;
    private int deliveryPosition;


    public RegionListAdapter(FragmentActivity activity, List<RegionListResponse.RegionList>
            bookingLists, RecyclerView menuList, RerfreshWishList activeRerfreshWishList) {
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
    public void onBindViewHolder(ViewHolder holder, final int position) {


            holder.region.setText(list.get(position).getRegion_name());


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