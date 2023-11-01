package com.arcltd.staff.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.addData.AddMoterCycleActivity;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.MoterCycleListResponse;
import com.arcltd.staff.utility.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MoterCycleListAdapter extends RecyclerView.Adapter<MoterCycleListAdapter.ViewHolder> {
    Context context;
    List<MoterCycleListResponse.MoterCycle> list;
    RecyclerView menuList;
    private MoterCycleListResponse wishList;
    private int deliveryPosition;
    private SoldVehicle soldVehicle;
    Context mContext;



    public MoterCycleListAdapter(FragmentActivity activity, List<MoterCycleListResponse.MoterCycle>
            bookingLists, RecyclerView menuList, MoterCycleListResponse activeRerfreshWishList,
                                 SoldVehicle soldVehi) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;
        this.soldVehicle=soldVehi;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_allmixed_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.branchCode.setText(list.get(position).getBranch_code());
        holder.item.setText(list.get(position).getVehicle_no());

        if (!list.get(position).getSold_status().equals("Y")){
            holder.cvView.setBackgroundColor(Color.WHITE);
        }else {
            holder.cvView.setBackgroundColor(Color.YELLOW);
        }


        holder.listRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final BottomSheetDialog dialog = new BottomSheetDialog(context);
                dialog.setContentView(R.layout.item_motercycle_list);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow()
                        .setLayout(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );


                try {
                    TextView tvBranchCode,tvVehicleno,tvRCLastDate,tvMake,tvMpdel,tvInsUpTo,tvstdKm,tvInsNo,
                            tvUserName;
                    ImageView edit;
                    TextView btnSold;

                    tvBranchCode = dialog.findViewById(R.id.tvBranchCode);
                    tvVehicleno = dialog.findViewById(R.id.tvVehicleno);
                    tvRCLastDate = dialog.findViewById(R.id.tvRCLastDate);
                    tvMake = dialog.findViewById(R.id.tvMake);
                    tvMpdel = dialog.findViewById(R.id.tvMpdel);
                    tvInsUpTo = dialog.findViewById(R.id.tvInsUpTo);
                   // tvPucUpto = dialog.findViewById(R.id.tvPucUpto);
                    edit = dialog.findViewById(R.id.edit);
                    btnSold = dialog.findViewById(R.id.btnSold);
                    tvstdKm = dialog.findViewById(R.id.tvStdKm);
                    tvInsNo = dialog.findViewById(R.id.tvInsNo);
                    tvUserName = dialog.findViewById(R.id.tvUserName);
                    btnSold.setVisibility(View.GONE);
                    edit.setVisibility(View.GONE);

                    if (list.get(position).getSold_status().equals("Y")){
                        btnSold.setVisibility(View.GONE);
                        edit.setVisibility(View.GONE);

                    }else {
                        btnSold.setVisibility(View.VISIBLE);
                        edit.setVisibility(View.VISIBLE);
                    }

                    tvBranchCode.setText(list.get(position).getBranch_code());
                    tvVehicleno.setText(list.get(position).getVehicle_no());
                    tvRCLastDate.setText(list.get(position).getReg_last_Date());
                    tvMake.setText(list.get(position).getMake());
                    tvMpdel.setText(list.get(position).getModel());
                    tvInsUpTo.setText(list.get(position).getIns_upto());

                    tvstdKm.setText(list.get(position).getStanderd_km());
                    tvInsNo.setText(list.get(position).getInsurance_no());
                    tvUserName.setText(list.get(position).getUser_person());
                   // tvPucUpto.setText(list.get(position).getPuc_upto());



                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String data = new Gson().toJson(list.get(position));
                            Log.e("TAG", "AddMoterCycleActivity: "+data );
                            context.startActivity(new Intent(context, AddMoterCycleActivity.class)
                                    .putExtra("data", data));

                        }
                    });

                    btnSold.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(context)
                                    .setTitle("Sold Out")
                                    .setMessage("Are you sure you want Sold this Moter Cycle?")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Continue with delete operation
                                            soldVehicle.soldVehi(list.get(position).getVehicle_no());
                                            dialog.dismiss();
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            dialog.dismiss();

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


    public interface SoldVehicle {
        void soldVehi(String vehicle_no);
    }

    public interface RerfreshWishList {
        void refresh(Boolean b);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView branchCode,item;
        LinearLayout listRegion;
        CardView cvView;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            branchCode = itemView.findViewById(R.id.branchCode);
            item = itemView.findViewById(R.id.item);
            listRegion = itemView.findViewById(R.id.listRegion);
            cvView = itemView.findViewById(R.id.cvView);

        }
    }

}
