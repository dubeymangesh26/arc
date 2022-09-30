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
import com.arcltd.staff.activity.addData.AddConveyanceMobileActivity;
import com.arcltd.staff.response.ConveyanceMobileListResponse;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EmployeeConveyanceListAdapter extends RecyclerView.Adapter<EmployeeConveyanceListAdapter.ViewHolder> {
    Context context;
    List<ConveyanceMobileListResponse.ConveyanceMobileList> list;
    RecyclerView menuList;
    private ConveyanceMobileListResponse wishList;
    private int deliveryPosition;


    public EmployeeConveyanceListAdapter(FragmentActivity activity, List<ConveyanceMobileListResponse.ConveyanceMobileList>
            bookingLists, RecyclerView menuList, ConveyanceMobileListResponse activeRerfreshWishList) {
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
        holder.item.setText(list.get(position).getEmp_name());

        holder.listRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.item_conveyancemobile_list);
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
                    TextView tvBranchCode,tvEmpCode,tvEmpName,tvConveyance,tvMobileExp;
                    ImageView edit;

                    tvBranchCode = dialog.findViewById(R.id.tvBranchCode);
                    tvEmpCode = dialog.findViewById(R.id.tvEmpCode);
                    tvEmpName = dialog.findViewById(R.id.tvEmpName);
                    tvConveyance = dialog.findViewById(R.id.tvConveyance);
                    tvMobileExp = dialog.findViewById(R.id.tvMobileExp);
                    edit = dialog.findViewById(R.id.edit);

                    tvBranchCode.setText(list.get(position).getBranch_code());
                    tvEmpCode.setText(list.get(position).getEmp_code());
                    tvEmpName.setText(list.get(position).getEmp_name());
                    tvConveyance.setText(list.get(position).getConveyance());
                    tvMobileExp.setText(list.get(position).getMobile());

                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String data = new Gson().toJson(list.get(position));
                            Log.e("TAG", "AddMoterCycleActivity: "+data );
                            context.startActivity(new Intent(context, AddConveyanceMobileActivity.class)
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
