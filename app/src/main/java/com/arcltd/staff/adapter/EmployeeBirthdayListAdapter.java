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
import com.arcltd.staff.activity.otherAndMain.BirthdayWisheshActivity;
import com.arcltd.staff.response.EmployeeListResponse;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EmployeeBirthdayListAdapter extends RecyclerView.Adapter<EmployeeBirthdayListAdapter.ViewHolder> {
    Context context;
    List<EmployeeListResponse.EmployeeList> list;
    RecyclerView menuList;
    private EmployeeListResponse wishList;
    private int deliveryPosition;


    public EmployeeBirthdayListAdapter(FragmentActivity activity, List<EmployeeListResponse.EmployeeList>
            bookingLists, RecyclerView menuList, EmployeeListResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_emplst_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.region.setText(list.get(position).getEmp_name());
        holder.branchCode.setText(list.get(position).getBranch_code());

        holder.listRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = new Gson().toJson(list.get(position));
                Log.e("TAG", "MessList: "+data );
                context.startActivity(new Intent(context, BirthdayWisheshActivity.class).putExtra("data", data));

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
        TextView region,branchCode;
        LinearLayout listRegion;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            region = itemView.findViewById(R.id.region);
            listRegion = itemView.findViewById(R.id.listRegion);
            branchCode = itemView.findViewById(R.id.branchCode);



        }
    }

}
