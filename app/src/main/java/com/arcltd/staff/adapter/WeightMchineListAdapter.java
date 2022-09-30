package com.arcltd.staff.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.addData.AddWeightMachineActivity;
import com.arcltd.staff.response.WeightMachineListResponse;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WeightMchineListAdapter extends RecyclerView.Adapter<WeightMchineListAdapter.ViewHolder> {
    Context context;
    List<WeightMachineListResponse.WeightMachine> list;
    RecyclerView menuList;
    private WeightMachineListResponse wishList;
    private int deliveryPosition;


    public WeightMchineListAdapter(FragmentActivity activity, List<WeightMachineListResponse.WeightMachine>
            bookingLists, RecyclerView menuList, WeightMachineListResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_allmixed_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.branchCode.setText(list.get(position).getBranch_code());
        holder.item.setText(list.get(position).getWeightMc_No());

        holder.listRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.item_weightmachine_list);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow()
                        .setLayout(
                                ViewGroup.LayoutParams.FILL_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );


                try {
                    TextView tvBranchCode,tvWeightMcno,tvMake,tvMpdel,tvRenewUpto;
                    ImageView edit;

                    tvBranchCode = dialog.findViewById(R.id.tvBranchCode);
                    tvWeightMcno = dialog.findViewById(R.id.tvWeightMcno);
                    tvMake = dialog.findViewById(R.id.tvMake);
                    tvMpdel = dialog.findViewById(R.id.tvMpdel);
                    tvRenewUpto = dialog.findViewById(R.id.tvRenewUpto);
                    edit = dialog.findViewById(R.id.edit);

                    tvBranchCode.setText(list.get(position).getBranch_code());
                    tvWeightMcno.setText(list.get(position).getWeightMc_No());
                    tvMake.setText(list.get(position).getMake());
                    tvMpdel.setText(list.get(position).getModel());
                    tvRenewUpto.setText(list.get(position).getRenew_upto());

                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String data = new Gson().toJson(list.get(position));
                            Log.e("TAG", "MessOfficeGodownList: "+data );
                            context.startActivity(new Intent(context, AddWeightMachineActivity.class)
                                    .putExtra("data", data));

                        }
                    });
                } catch (Exception e) {
                    Log.e("TAG", "onClick: ", e);
                }

                ImageView close = (ImageView) dialog.findViewById(R.id.close);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
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

        TextView branchCode,item;
        LinearLayout listRegion;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            branchCode = itemView.findViewById(R.id.branchCode);
            item = itemView.findViewById(R.id.item);
            listRegion = itemView.findViewById(R.id.listRegion);

        }
    }

}
