package com.arcltd.staff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.addData.AddSweeperPeonActivity;
import com.arcltd.staff.response.SweeperPeonListResponse;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PeonSweeperListAdapter extends RecyclerView.Adapter<PeonSweeperListAdapter.ViewHolder> {
    Context context;
    List<SweeperPeonListResponse.SweeperPeonList> list;
    RecyclerView menuList;
    private SweeperPeonListResponse wishList;
    private int deliveryPosition;


    public PeonSweeperListAdapter(FragmentActivity activity, List<SweeperPeonListResponse.SweeperPeonList>
            bookingLists, RecyclerView menuList, SweeperPeonListResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_sweeperpeon_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.tvBranchCode.setText(list.get(position).getBranch_code());
        holder.tvSalary.setText(list.get(position).getEmp_salary());
        holder.tvSweeperPeonName.setText(list.get(position).getEmp_name());
        if (list.get(position).getSweeper_peon().equals("S")){
            holder.swpoName.setText("Sweeper");
        }else {
            holder.swpoName.setText("Peon");
        }

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = new Gson().toJson(list.get(position));
                Log.e("TAG", "AddMoterCycleActivity: "+data );
                context.startActivity(new Intent(context, AddSweeperPeonActivity.class)
                        .putExtra("data", data));

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
        TextView tvBranchCode,swpoName,tvSweeperPeonName,tvSalary;
        ImageView edit;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvBranchCode = itemView.findViewById(R.id.tvBranchCode);
            swpoName = itemView.findViewById(R.id.swpoName);
            tvSweeperPeonName = itemView.findViewById(R.id.tvSweeperPeonName);
            tvSalary = itemView.findViewById(R.id.tvSalary);
            edit = itemView.findViewById(R.id.edit);



        }
    }

}
